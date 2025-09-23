package com.evolvedbinary.xth.tsom.result.impl;

import com.evolvedbinary.xth.tsom.result.TestCaseResult;

public abstract sealed class AbstractTestCaseResult implements TestCaseResult permits TestCaseResultPassImpl, TestCaseResultFailureImpl, TestCaseResultErrorImpl {
    private final long executionTime;

    protected AbstractTestCaseResult(final long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }
}
