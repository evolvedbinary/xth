/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
package com.evolvedbinary.xth.connector.impl;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.connector.api.TestCaseExecutionContext;
import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.EnvironmentReference;
import com.evolvedbinary.xth.tsom.Specification;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.dependency.SpecificationDependency;
import com.evolvedbinary.xth.tsom.dependency.SpecificationVersionDescription;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;
import static com.evolvedbinary.xth.util.MapUtil.toImmutableMap;

public abstract class AbstractConnector<T extends TestCaseExecutionContext> implements Connector<T> {

    private static final SpecificationVersion DEFAULT_XQUERY_VERSION = SpecificationVersion.XQUERY_3_1;
    private static final SpecificationVersion DEFAULT_XPATH_VERSION = SpecificationVersion.XPATH_3_1;

    private final Path baseUri;
    private final EnumSet<SpecificationVersion> defaultSpecifications;
    private final Map<String, EnvironmentDefinition> globalEnvironments;

    public AbstractConnector(final Path baseUri, final EnumSet<SpecificationVersion> defaultSpecifications, final List<EnvironmentDefinition> globalEnvironments) {
        this.baseUri = baseUri;
        this.defaultSpecifications = defaultSpecifications;
        this.globalEnvironments = new HashMap<>(globalEnvironments.size());
        for (final EnvironmentDefinition globalEnvironment : globalEnvironments) {
            this.globalEnvironments.put(globalEnvironment.getName(), globalEnvironment);
        }
    }

    @Override
    public List<Dependency<?>> supports(final List<Dependency<?>> dependencies) {
        if (dependencies.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Dependency<?>> unsupportedDependencies = new ArrayList<>();
        for (final Dependency<?> dependency : dependencies) {
            final boolean supported = supportsDependency(dependency);

            if ((dependency.isSatisfied() && !supported) || ((!dependency.isSatisfied()) && supported)) {
                unsupportedDependencies.add(dependency);
            }
        }
        return unsupportedDependencies;
    }

    protected boolean supportsDependency(final Dependency<?> dependency) {
        final Set<Dependency<?>> supportedDependencies = getSupportedDependencies();
        if (supportedDependencies.contains(dependency)) {
            return true;
        }

        if (dependency instanceof SpecificationDependency specificationDependency) {
            for (final Dependency<?> supportedDependency: supportedDependencies) {
                if (supportedDependency instanceof SpecificationDependency supportedSpecificationDependency) {
                    if (specificationDependency.isCompatibleWith(supportedSpecificationDependency)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected abstract Set<Dependency<?>> getSupportedDependencies();

    protected static List<SpecificationDependency> getTestCaseSpecifications(final TestSet testSet, final TestCase testCase) {
        // test case dependencies first
        final List<SpecificationDependency> specificationDependencies = new ArrayList<>();
        for (final Dependency<?> testCaseDependency : testCase.getDependencies()) {
            if (testCaseDependency instanceof SpecificationDependency specificationDependency) {
                specificationDependencies.add(specificationDependency);
            }
        }

        // test set dependencies second
        for (final Dependency<?> testSetDependency : testSet.getDependencies()) {
            if (testSetDependency instanceof SpecificationDependency specificationDependency) {
                specificationDependencies.add(specificationDependency);
            }
        }

        return specificationDependencies;
    }

    protected SpecificationVersion[] getXPathAndXQueryVersion(final List<SpecificationDependency> specificationDependencies) {
        return getXPathAndXQueryVersion(specificationDependencies, defaultSpecifications);
    }

    protected SpecificationVersion[] getXPathAndXQueryVersion(final List<SpecificationDependency> specificationDependencies, final EnumSet<SpecificationVersion> defaultSpecifications) {
        SpecificationVersion xpathSpecificationVersion = null;
        SpecificationVersion xquerySpecificationVersion = null;

        for (final SpecificationDependency specificationDependency : specificationDependencies) {
            if (specificationDependency.isSatisfied()) {
                for (final SpecificationVersionDescription specificationVersionDescription : specificationDependency.getDependencyValue()) {
                    SpecificationVersion specificationVersion = specificationVersionDescription.getSpecificationVersion();

                    // are we permitted to use a newer version?
                    if (specificationVersionDescription.isNewerVersionAllowed()) {

                        // find the maximum version permitted by the Test Suite
                        @Nullable SpecificationVersion maxDefaultSpecification = null;
                        for (final SpecificationVersion defaultSpecification : defaultSpecifications) {
                            if (specificationVersion.getSpecification() == defaultSpecification.getSpecification()) {
                                maxDefaultSpecification = defaultSpecification;
                            }
                        }

                        if (maxDefaultSpecification != null) {
                            final SpecificationVersion[] newerVersions = specificationVersion.getNewerVersions();
                            for (final SpecificationVersion newerVersion : newerVersions) {
                                // newerVersion must be: greater than specificationVersion AND less than or equal to defaultSpecification (the max version allowable by the Test Suite)
                                if (newerVersion.compare(specificationVersion) > 0 && newerVersion.compare(maxDefaultSpecification) <= 0) {
                                    specificationVersion = newerVersion;
                                }
                            }
                        }
                    }

                    if (specificationVersion.getSpecification() == Specification.XPATH) {
                        if (xpathSpecificationVersion == null) {
                            xpathSpecificationVersion = specificationVersion;
                        } else {
                            // When multiple versions of XPath are specified, we use the latest version
                            if (xpathSpecificationVersion.compare(specificationVersion) < 0) {
                                xpathSpecificationVersion = specificationVersion;
                            }
                        }

                    } else if (specificationVersion.getSpecification() == Specification.XQUERY) {
                        if (xquerySpecificationVersion == null) {
                            xquerySpecificationVersion = specificationVersion;
                        } else {
                            // When multiple versions of XQuery are specified, we use the latest version
                            if (xquerySpecificationVersion.compare(specificationVersion) < 0) {
                                xquerySpecificationVersion = specificationVersion;
                            }
                        }
                    }
                }
            }
        }

        // if XPath or XQuery version was not found in the specificationDependencies, fallback to the default specification versions
        int needSpecifications = 0;
        if (xpathSpecificationVersion == null) {
            needSpecifications++;
        }
        if (xquerySpecificationVersion == null) {
            needSpecifications++;
        }
        int foundSpecifications = 0;
        if (xpathSpecificationVersion == null || xquerySpecificationVersion == null) {
            for (final SpecificationVersion defaultSpecification : defaultSpecifications) {
                if (xpathSpecificationVersion == null && defaultSpecification.getSpecification() == Specification.XPATH) {
                    xpathSpecificationVersion = defaultSpecification;
                    foundSpecifications++;
                }

                if (xquerySpecificationVersion == null && defaultSpecification.getSpecification() == Specification.XQUERY) {
                    // first the global version from the catalog
                    xquerySpecificationVersion = defaultSpecification;
                    foundSpecifications++;
                }

                if (needSpecifications == foundSpecifications) {
                    break;
                }
            }
        }

        // finally, if XPath or XQuery version was still not found use the constant defaults
        if (xpathSpecificationVersion == null) {
            xpathSpecificationVersion = getDefaultXPathVersion();

        }
        if (xquerySpecificationVersion == null) {
            xquerySpecificationVersion = getDefaultXQueryVersion();
        }

        return new SpecificationVersion[] { xpathSpecificationVersion, xquerySpecificationVersion };
    }

    protected Map<String, EnvironmentDefinition> resolveTestSetEnvironments(final TestSet testSet) throws ConnectorException {
        final List<Environment> testSetEnvironments = testSet.getEnvironments();
        final Map<String, EnvironmentDefinition> results = new HashMap<>(testSetEnvironments.size());
        for (final Environment testSetEnvironment : testSetEnvironments) {
            final EnvironmentDefinition environment = resolveEnvironment(testSetEnvironment, globalEnvironments);
            results.put(environment.getName(), environment);
        }
        return toImmutableMap(results);
    }

    protected List<EnvironmentDefinition> getTestCaseEnvironments(final Map<String, EnvironmentDefinition> testSetEnvironments, final TestCase testCase) throws ConnectorException {
        final List<EnvironmentDefinition> results = new ArrayList<>(testSetEnvironments.size());
        for (final Environment testCaseEnvironment : testCase.getEnvironments()) {
            final EnvironmentDefinition environment = resolveEnvironment(testCaseEnvironment, globalEnvironments, testSetEnvironments);
            results.add(environment);
        }
        return toImmutableList(results);
    }

    private static EnvironmentDefinition resolveEnvironment(final Environment environment, final Map<String, EnvironmentDefinition>... environmentDefinitionss) throws ConnectorException {

        if (environment instanceof EnvironmentReference) {
            // de-reference
            EnvironmentDefinition result = null;
            for (final Map<String, EnvironmentDefinition> environmentDefinitions : environmentDefinitionss) {
                result = environmentDefinitions.get(environment.getName());
                if (result != null) {
                    return result;
                }
            }

            if (result == null) {
                throw new ConnectorException("Unable to resolve environment: " + environment.getName());
            }

            return result;

        } else if (environment instanceof final EnvironmentDefinition environmentDefinition) {
            return environmentDefinition;

        } else {
            throw new ConnectorException("Unknown environment type: " + environment.getClass().getName());
        }
    }

    protected SpecificationVersion getDefaultXPathVersion() {
        return DEFAULT_XPATH_VERSION;
    }

    protected SpecificationVersion getDefaultXQueryVersion() {
        return DEFAULT_XQUERY_VERSION;
    }
}
