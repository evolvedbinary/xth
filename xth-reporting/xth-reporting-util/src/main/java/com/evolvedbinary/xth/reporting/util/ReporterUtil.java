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
package com.evolvedbinary.xth.reporting.util;

import com.evolvedbinary.xth.reporting.api.Reporter;
import com.evolvedbinary.xth.reporting.api.ReporterConfiguration;
import com.evolvedbinary.xth.reporting.spi.ReporterProvider;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class ReporterUtil {

    public static @Nullable List<Reporter> reporters(final ReporterConfiguration reporterConfiguration) {
        final ServiceLoader<ReporterProvider> serviceLoader = ServiceLoader.load(ReporterProvider.class);
        final Iterator<ReporterProvider> itServiceLoader = serviceLoader.iterator();

        List<Reporter> reporters = null;

        while (itServiceLoader.hasNext()) {
            final ReporterProvider reporterProvider = itServiceLoader.next();
            final Reporter reporter = reporterProvider.newReporter(reporterConfiguration);
            if (reporter != null) {
                if (reporters == null) {
                    reporters = new ArrayList<>();
                }
                reporters.add(reporter);
            }
        }

        if (reporters != null) {
            // make returned list immutable
            reporters = Collections.unmodifiableList(reporters);
        }

        return reporters;
    }
}
