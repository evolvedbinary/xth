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
package com.evolvedbinary.xth.connector.elemental.embedded;

import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.connector.impl.AbstractConnector;
import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ElementalEmbeddedConnector extends AbstractConnector<ElementalEmbeddedTestCaseExecutionContext> {

    ElementalEmbeddedConnector(final Path baseUri, final EnumSet<SpecificationVersion> defaultSpecifications, final List<EnvironmentDefinition> globalEnvironments) {
        super(baseUri, defaultSpecifications, globalEnvironments);
    }

    @Override
    public String getImplementationName() throws ConnectorException {
        throw new ConnectorException(new UnsupportedOperationException("TODO(AR) implement"));
    }

    @Override
    public String getImplementationVersion() throws ConnectorException {
        throw new ConnectorException(new UnsupportedOperationException("TODO(AR) implement"));
    }

    @Override
    public List<Dependency<?>> supports(final List<Dependency<?>> dependencies) {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }

    @Override
    protected Set<Dependency<?>> getSupportedDependencies() {
        return Set.of();
    }

    @Override
    public ElementalEmbeddedTestCaseExecutionContext prepareTestCaseForExecution(final TestSet testSet, final TestCase testCase) throws ConnectorException {
        throw new ConnectorException(new UnsupportedOperationException("TODO(AR) implement"));
    }

    @Override
    public TestCaseResult executeTestCase(final ElementalEmbeddedTestCaseExecutionContext testCaseExecutionContext) {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }
}
