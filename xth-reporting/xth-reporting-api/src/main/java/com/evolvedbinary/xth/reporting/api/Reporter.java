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
package com.evolvedbinary.xth.reporting.api;

/**
 * Interface for a Reporter that can report
 * on the results of executing test cases.
 *
 * Reporters produce results listeners, whose
 * job it is to collect/collate/format and output
 * the results of executing test cases.
 */
public interface Reporter {

    /**
     * Get the name of the reporter.
     *
     * Should be a short but humane name.
     *
     * @return the name of the reporter.
     */
    default String getReporterName() {
        return getClass().getSimpleName();
    }

    /**
     * Open a new listener for the reporter.
     *
     * @return a listener that listens for test results.
     *
     * @throws ReporterException if an error occurs when opening a new listener.
     */
    TestResultsListener open() throws ReporterException;
}
