package com.evolvedbinary.xth.tsom.result.impl.compiled;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResultFailure;

import java.time.Instant;

public final class CompiledTestCaseResultFailureImpl extends AbstractCompiledTestCaseResult implements CompiledTestCaseResultFailure {
    public CompiledTestCaseResultFailureImpl(final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, final Instant processingFinished) {
        super(processingStarted, compilationStarted, compilationFinished, processingFinished);
    }
}
