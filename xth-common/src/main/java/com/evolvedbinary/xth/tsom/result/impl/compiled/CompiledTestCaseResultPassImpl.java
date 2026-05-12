package com.evolvedbinary.xth.tsom.result.impl.compiled;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResultPass;

import java.time.Instant;

public final class CompiledTestCaseResultPassImpl extends AbstractCompiledTestCaseResult implements CompiledTestCaseResultPass {
    public CompiledTestCaseResultPassImpl(final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, final Instant processingFinished) {
        super(processingStarted, compilationStarted, compilationFinished, processingFinished);
    }
}
