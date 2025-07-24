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
package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.connector.api.ConnectorFactory;
import com.evolvedbinary.xth.connector.api.TestCaseExecutionContext;
import com.evolvedbinary.xth.parser.api.ParserException;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

class ExecuteTestSuiteTask implements Callable<Void> {

    private final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory;
    private final TestSuiteParser parser;
    private @Nullable final List<TestResultsListener> testResultsListeners;

    public ExecuteTestSuiteTask(final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory, final TestSuiteParser parser, @Nullable final List<TestResultsListener> testResultsListeners) {
        this.connectorFactory = connectorFactory;
        this.parser = parser;
        this.testResultsListeners = testResultsListeners;
    }

    @Override
    public Void call() throws InterruptedException, ExecuteTestSuiteTaskException {
        final ThreadFactory testSuiteThreadFactory = Thread.ofVirtual().name("test-suite-0-thread", 0).factory();
        try (final StructuredTaskScope<Void, Void> testSuiteScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow(), config -> config.withThreadFactory(testSuiteThreadFactory))) {
            final TestSuiteExecutionDispatcher testSuiteExecutionDispatcher = new TestSuiteExecutionDispatcher(connectorFactory, testSuiteScope);
            if (testResultsListeners != null) {
                for (final TestResultsListener testResultsListener : testResultsListeners) {
                    testSuiteExecutionDispatcher.addTestResultsListener(testResultsListener);
                }
            }
            parser.addEventListener(testSuiteExecutionDispatcher);

            try {
                parser.parse();
            } catch (final IOException | ParserException e) {
                throw new ExecuteTestSuiteTaskException(e);
            }

            testSuiteScope.join();
        }

        return null;
    }

    static class ExecuteTestSuiteTaskException extends Exception {
        public ExecuteTestSuiteTaskException(final String message) {
            super(message);
        }

        public ExecuteTestSuiteTaskException(final Throwable throwable) {
            super(throwable);
        }

        public ExecuteTestSuiteTaskException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
