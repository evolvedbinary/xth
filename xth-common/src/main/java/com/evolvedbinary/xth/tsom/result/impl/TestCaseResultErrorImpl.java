package com.evolvedbinary.xth.tsom.result.impl;

import com.evolvedbinary.xth.tsom.result.TestCaseResultError;

public final class TestCaseResultErrorImpl extends AbstractTestCaseResult implements TestCaseResultError {
    public TestCaseResultErrorImpl(final long executionTime) {
        super(executionTime);
    }
}
