package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.TestCaseResultError;
import com.evolvedbinary.xth.tsom.result.TestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.TestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.TestCaseResultSkipped;

import java.util.function.Consumer;

import static com.evolvedbinary.xth.util.TimeUtil.toHumaneString;

public abstract class AbstractTestResultsListenerPrinter implements TestResultsListener {

    private final Consumer<String> printer;

    protected AbstractTestResultsListenerPrinter(final Consumer<String> printer) {
        this.printer = printer;
    }

    @Override
    public void result(final TestSet testSet, final TestCase testCase, final TestCaseResult testCaseResult) {
        final String resultStr = switch (testCaseResult) {
            case TestCaseResultPass testCaseResultPass -> "Pass";
            case TestCaseResultFailure testCaseResultFailure -> "FAIL"; // TODO(AR) add failure info?
            case TestCaseResultError testCaseResultError -> "ERROR";  // TODO(AR) add error info?
            case TestCaseResultSkipped testCaseResultSkipped -> "Skipped";  // TODO(AR) add reason info?
        };
        printer.accept(String.format("%s / %s (%s): %s", testSet.getName(), testCase.getName(), toHumaneString(testCaseResult.getExecutionTime()), resultStr));
    }
}
