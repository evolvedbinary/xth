package com.evolvedbinary.xth.tsom.result.impl.compiled;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResultError;

import java.time.Instant;

public final class CompiledTestCaseResultErrorImpl extends AbstractCompiledTestCaseResult implements CompiledTestCaseResultError {
    public CompiledTestCaseResultErrorImpl(final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, final Instant processingFinished) {
        super(processingStarted, compilationStarted, compilationFinished, processingFinished);
    }
}
