package com.evolvedbinary.xth.tsom.result.impl;

import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.compiled.AbstractCompiledTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.executed.AbstractExecutedTestCaseResult;

import java.time.Instant;

public abstract sealed class AbstractTestCaseResult implements TestCaseResult permits AbstractCompiledTestCaseResult, AbstractExecutedTestCaseResult, TestCaseResultSkippedImpl {
    private final Instant processingStarted;
    private final Instant processingFinished;

    protected AbstractTestCaseResult(final Instant processingStarted, final Instant processingFinished) {
        this.processingStarted = processingStarted;
        this.processingFinished = processingFinished;
    }

    @Override
    public Instant getProcessingStarted() {
        return processingStarted;
    }

    @Override
    public Instant getProcessingFinished() {
        return processingFinished;
    }
}
