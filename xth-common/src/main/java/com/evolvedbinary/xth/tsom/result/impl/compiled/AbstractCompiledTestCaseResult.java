package com.evolvedbinary.xth.tsom.result.impl.compiled;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.AbstractTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.compiled.executed.AbstractCompiledExecutedTestCaseResult;

import java.time.Instant;

public sealed abstract class AbstractCompiledTestCaseResult extends AbstractTestCaseResult implements CompiledTestCaseResult permits CompiledTestCaseResultPassImpl, CompiledTestCaseResultFailureImpl, CompiledTestCaseResultErrorImpl, AbstractCompiledExecutedTestCaseResult {
    private final Instant compilationStarted;
    private final Instant compilationFinished;

    protected AbstractCompiledTestCaseResult(final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, final Instant processingFinished) {
        super(processingStarted, processingFinished);
        this.compilationStarted = compilationStarted;
        this.compilationFinished = compilationFinished;
    }

    @Override
    public Instant getCompilationStarted() {
        return compilationStarted;
    }

    @Override
    public Instant getCompilationFinished() {
        return compilationFinished;
    }
}
