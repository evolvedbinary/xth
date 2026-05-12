package com.evolvedbinary.xth.tsom.result.impl.executed;

import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResultFailure;

import java.time.Instant;

public final class ExecutedTestCaseResultFailureImpl extends AbstractExecutedTestCaseResult implements ExecutedTestCaseResultFailure {
    public ExecutedTestCaseResultFailureImpl(final Instant processingStarted, final Instant executionStarted, final Instant executionFinished, final Instant processingFinished) {
        super(processingStarted, executionStarted, executionFinished, processingFinished);
    }
}
