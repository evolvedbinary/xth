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
package com.evolvedbinary.xth.connector.util;

import com.evolvedbinary.xth.connector.api.ConnectorFactory;
import com.evolvedbinary.xth.connector.api.TestCaseExecutionContext;
import com.evolvedbinary.xth.connector.spi.ConnectorFactoryProvider;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class ConnectorFactoryUtil {

    public static @Nullable List<ConnectorFactory<? extends TestCaseExecutionContext>> connectorFactories() {
        final ServiceLoader<ConnectorFactoryProvider> serviceLoader = ServiceLoader.load(ConnectorFactoryProvider.class);
        final Iterator<ConnectorFactoryProvider> itServiceLoader = serviceLoader.iterator();

        List<ConnectorFactory<? extends TestCaseExecutionContext>> connectorFactories = null;

        while (itServiceLoader.hasNext()) {
            final ConnectorFactoryProvider connectorFactoryProvider = itServiceLoader.next();
            final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory = connectorFactoryProvider.newConnectorFactory();
            if (connectorFactory != null) {
                if (connectorFactories == null) {
                    connectorFactories = new ArrayList<>();
                }
                connectorFactories.add(connectorFactory);
            }
        }

        if (connectorFactories != null) {
            // make returned list immutable
            connectorFactories = Collections.unmodifiableList(connectorFactories);
        }

        return connectorFactories;
    }
}
