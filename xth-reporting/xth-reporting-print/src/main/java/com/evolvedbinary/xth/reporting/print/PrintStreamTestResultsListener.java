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
package com.evolvedbinary.xth.reporting.print;

import com.evolvedbinary.xth.reporting.api.ReporterConfiguration;
import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;
import net.jcip.annotations.ThreadSafe;

import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@ThreadSafe
public class PrintStreamTestResultsListener extends AbstractPrintTestResultsListener {

    private final boolean verbose;

    private PrintStreamTestResultsListener(final PrintStream printStream, final boolean verbose) {
        super(printStream::println);
        this.verbose = verbose;
    }

    public static TestResultsListener open(final ReporterConfiguration reporterConfiguration) {
        return new PrintStreamTestResultsListener(reporterConfiguration.stdOut(), reporterConfiguration.verbose());
    }

    @Override
    public void testSetExcluded(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        if (verbose) {
            super.testSetExcluded(testSuite, testSetId, testSetFile, testSetName, timestamp);
        }
    }

    @Override
    public void testCaseExcluded(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        if (verbose) {
            super.testCaseExcluded(testSuite, testSet, testCaseId, testCaseName, timestamp);
        }
    }
}
