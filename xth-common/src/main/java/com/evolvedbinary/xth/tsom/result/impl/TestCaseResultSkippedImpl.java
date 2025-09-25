package com.evolvedbinary.xth.tsom.result.impl;

import com.evolvedbinary.xth.tsom.result.TestCaseResultSkipped;

public final class TestCaseResultSkippedImpl extends AbstractTestCaseResult implements TestCaseResultSkipped {
    private final String reason;

    public TestCaseResultSkippedImpl(final String reason) {
        super(0);
        this.reason = reason;
    }
}
