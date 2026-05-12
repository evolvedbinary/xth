package com.evolvedbinary.xth.tsom.result.impl.executed;

import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.AbstractTestCaseResult;

import java.time.Instant;

public abstract sealed class AbstractExecutedTestCaseResult extends AbstractTestCaseResult implements ExecutedTestCaseResult permits ExecutedTestCaseResultPassImpl, ExecutedTestCaseResultFailureImpl, ExecutedTestCaseResultErrorImpl {
    private final Instant executionStarted;
    private final Instant executionFinished;

    AbstractExecutedTestCaseResult(final Instant processingStarted, final Instant executionStarted, final Instant executionFinished, final Instant processingFinished) {
        super(processingStarted, processingFinished);
        this.executionStarted = executionStarted;
        this.executionFinished = executionFinished;
    }

    @Override
    public Instant getExecutionStarted() {
        return executionStarted;
    }

    @Override
    public Instant getExecutionFinished() {
        return executionFinished;
    }
}
