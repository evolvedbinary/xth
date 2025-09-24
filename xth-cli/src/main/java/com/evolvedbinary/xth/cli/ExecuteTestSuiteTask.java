package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.parser.api.ParserException;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

class ExecuteTestSuiteTask implements Callable<Void> {

    private final Connector connector;
    private final TestSuiteParser parser;
    private @Nullable final List<TestResultsListener> testResultsListeners;

    public ExecuteTestSuiteTask(final Connector connector, final TestSuiteParser parser, @Nullable final List<TestResultsListener> testResultsListeners) {
        this.connector = connector;
        this.parser = parser;
        this.testResultsListeners = testResultsListeners;
    }

    @Override
    public Void call() throws InterruptedException, ExecuteTestSuiteTaskException {
        final ThreadFactory testCaseThreadFactory = Thread.ofVirtual().name("test-case-", 0).factory();
        try (final StructuredTaskScope<Void, Void> testCaseScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow(), config -> config.withThreadFactory(testCaseThreadFactory))) {
            final TestCaseExecutionDispatcher testCaseExecutionDispatcher = new TestCaseExecutionDispatcher(connector, testCaseScope);
            if (testResultsListeners != null) {
                for (final TestResultsListener testResultsListener : testResultsListeners) {
                    testCaseExecutionDispatcher.addTestResultsListener(testResultsListener);
                }
            }
            parser.addEventListener(testCaseExecutionDispatcher);

            try {
                parser.parse();
            } catch (final IOException | ParserException e) {
                throw new ExecuteTestSuiteTaskException(e);
            }

            testCaseScope.join();
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
