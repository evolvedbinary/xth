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

import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;
import com.evolvedbinary.xth.tsom.result.*;
import net.jcip.annotations.ThreadSafe;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static com.evolvedbinary.xth.util.TimeUtil.toHumaneString;

@ThreadSafe
public abstract class AbstractPrintTestResultsListener implements TestResultsListener {

    private volatile @Nullable Consumer<String> printer;

    AbstractPrintTestResultsListener(final Consumer<String> printer) {
        this.printer = printer;
    }

    @Override
    public void testSuiteStarted(final TestSuite testSuite, final Instant timestamp) {
        throwIfClosed();
        // no-op
    }

    @Override
    public void testSuiteFinished(final TestSuite testSuite, final Instant timestamp) {
        throwIfClosed();
        // no-op
    }

    @Override
    public void testSetExcluded(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        throwIfClosed();
        printer.accept(String.format("%s (%s): Excluded", testSetName, toHumaneString(0)));
    }

    @Override
    public void testSetSkipped(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        throwIfClosed();
        printer.accept(String.format("%s (%s): Skipped", testSetName, toHumaneString(0)));
    }

    @Override
    public void testSetStarted(final TestSuite testSuite, final TestSet testSet, final Instant timestamp) {
        throwIfClosed();
        // no-op
    }

    @Override
    public void testSetFinished(final TestSuite testSuite, final TestSet testSet, final Instant timestamp) {
        throwIfClosed();
        // no-op
    }

    @Override
    public void testCaseExcluded(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        if (printer == null) {
            throw new IllegalStateException("setOutput(PrintStream) must be called before results are produced");
        }
        printer.accept(String.format("%s / %s (%s): Excluded", testSet.getName(), testCaseName, toHumaneString(0)));
    }

    @Override
    public void testCaseSkipped(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        if (printer == null) {
            throw new IllegalStateException("setOutput(PrintStream) must be called before results are produced");
        }
        printer.accept(String.format("%s / %s (%s): Skipped", testSet.getName(), testCaseName, toHumaneString(0)));
    }

    @Override
    public void testCaseStarted(final TestSuite testSuite, final TestSet testSet, final TestCase testCase, final Instant timestamp) {
        throwIfClosed();
        // no-op
    }

    @Override
    public void testCaseFinished(final TestSuite testSuite, final TestSet testSet, final TestCase testCase, final Instant timestamp, final TestCaseResult testCaseResult) {
        throwIfClosed();

        final String resultStr = switch (testCaseResult) {
            case TestCaseResultPass testCaseResultPass -> "Pass";
            case TestCaseResultFailure testCaseResultFailure -> "FAIL"; // TODO(AR) add failure info?
            case TestCaseResultError testCaseResultError -> "ERROR";  // TODO(AR) add error info?
            case TestCaseResultSkipped testCaseResultSkipped -> "Skipped";  // TODO(AR) add reason info?
        };
        final Duration duration = Duration.between(testCaseResult.getProcessingStarted(), testCaseResult.getProcessingFinished());
        printer.accept(String.format("%s / %s (%s): %s", testSet.getName(), testCase.getName(), toHumaneString(duration.toMillis()), resultStr));
    }

    private void throwIfClosed() {
        if (printer == null) {
            throw new IllegalStateException("AbstractPrintTestResultsListener has already been closed");
        }
    }

    @Override
    public void close() {
        if (printer != null) {
            printer = null;
        }
    }
}
