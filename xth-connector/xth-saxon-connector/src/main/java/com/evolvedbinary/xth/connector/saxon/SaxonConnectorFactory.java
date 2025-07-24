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
package com.evolvedbinary.xth.connector.saxon;

import com.evolvedbinary.xth.connector.api.ConnectorFactory;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import net.sf.saxon.Configuration;
import net.sf.saxon.Version;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;

public class SaxonConnectorFactory implements ConnectorFactory<SaxonTestCaseExecutionContext> {

    private final Configuration configuration;

    SaxonConnectorFactory() {
        // TODO(AR) should we allow Saxon configuration to be specified?
        // TODO(AR) is it okay to share one Saxon Configuration across multiple Saxon Processor
        this.configuration = Configuration.newLicensedConfiguration();
        this.configuration.setLogger(new SaxonLogger());
    }

    @Override
    public String getImplementationName() {
        return configuration.getEditionCode();
    }

    @Override
    public String getImplementationVersion() {
        return Version.getProductVersion();
    }

    @Override
    public SaxonConnector newConnector(final Path baseUri, final EnumSet<SpecificationVersion> defaultSpecifications, final List<EnvironmentDefinition> globalEnvironments) {
        return new SaxonConnector(configuration, baseUri, defaultSpecifications, globalEnvironments);
    }
}
