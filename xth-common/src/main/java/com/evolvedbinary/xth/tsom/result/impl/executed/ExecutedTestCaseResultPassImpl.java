package com.evolvedbinary.xth.tsom.result.impl.executed;

import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResultPass;

import java.time.Instant;

public final class ExecutedTestCaseResultPassImpl extends AbstractExecutedTestCaseResult implements ExecutedTestCaseResultPass {
    public ExecutedTestCaseResultPassImpl(final Instant processingStarted, final Instant executionStarted, final Instant executionFinished, final Instant processingFinished) {
        super(processingStarted, executionStarted, executionFinished, processingFinished);
    }
}
