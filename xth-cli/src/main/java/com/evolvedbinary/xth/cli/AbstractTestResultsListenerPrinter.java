package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.*;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import static com.evolvedbinary.xth.util.TimeUtil.toHumaneString;

public abstract class AbstractTestResultsListenerPrinter implements TestResultsListener {

    private final Consumer<String> printer;

    protected AbstractTestResultsListenerPrinter(final Consumer<String> printer) {
        this.printer = printer;
    }

    @Override
    public void testSetStarted(final TestSet testSet, final Instant timestamp) {
        // no-op
    }

    @Override
    public void testSetFinished(final TestSet testSet, final Instant timestamp) {
        // no-op
    }

    @Override
    public void testCaseStarted(final TestSet testSet, final TestCase testCase, final Instant timestamp) {
        // no-op
    }

    @Override
    public void testCaseFinished(final TestSet testSet, final TestCase testCase, final Instant timestamp, final TestCaseResult testCaseResult) {
        final String resultStr = switch (testCaseResult) {
            case TestCaseResultPass testCaseResultPass -> "Pass";
            case TestCaseResultFailure testCaseResultFailure -> "FAIL"; // TODO(AR) add failure info?
            case TestCaseResultError testCaseResultError -> "ERROR";  // TODO(AR) add error info?
            case TestCaseResultSkipped testCaseResultSkipped -> "Skipped";  // TODO(AR) add reason info?
        };
        final Duration duration = Duration.between(testCaseResult.getProcessingStarted(), testCaseResult.getProcessingFinished());
        printer.accept(String.format("%s / %s (%s): %s", testSet.getName(), testCase.getName(), toHumaneString(duration.toMillis()), resultStr));
    }
}
