package com.evolvedbinary.xth.tsom.result.impl.compiled.executed;

import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.compiled.AbstractCompiledTestCaseResult;

import java.time.Instant;

public sealed abstract class AbstractCompiledExecutedTestCaseResult extends AbstractCompiledTestCaseResult implements CompiledExecutedTestCaseResult permits CompiledExecutedTestCaseResultPassImpl, CompiledExecutedTestCaseResultFailureImpl, CompiledExecutedTestCaseResultErrorImpl {
    private final Instant executionStarted;
    private final Instant executionFinished;

    protected AbstractCompiledExecutedTestCaseResult(final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, final Instant executionStarted, final Instant executionFinished, final Instant processingFinished) {
        super(processingStarted, compilationStarted, compilationFinished, processingFinished);
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
