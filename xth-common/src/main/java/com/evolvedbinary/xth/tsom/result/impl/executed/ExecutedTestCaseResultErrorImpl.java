package com.evolvedbinary.xth.tsom.result.impl.executed;

import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResultError;

import java.time.Instant;

public final class ExecutedTestCaseResultErrorImpl extends AbstractExecutedTestCaseResult implements ExecutedTestCaseResultError {
    public ExecutedTestCaseResultErrorImpl(final Instant processingStarted, final Instant executionStarted, final Instant executionFinished, final Instant processingFinished) {
        super(processingStarted, executionStarted, executionFinished, processingFinished);
    }
}
