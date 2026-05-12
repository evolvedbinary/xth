package com.evolvedbinary.xth.tsom.result.impl;

import com.evolvedbinary.xth.tsom.result.TestCaseResultSkipped;

import java.time.Instant;

public final class TestCaseResultSkippedImpl extends AbstractTestCaseResult implements TestCaseResultSkipped {
    private final String reason;

    public TestCaseResultSkippedImpl(final Instant timestamp, final String reason) {
        super(timestamp, timestamp);
        this.reason = reason;
    }
}
