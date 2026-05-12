package com.evolvedbinary.xth.tsom.result.impl.compiled.executed;

import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResultFailure;

import java.time.Instant;

public final class CompiledExecutedTestCaseResultFailureImpl extends AbstractCompiledExecutedTestCaseResult implements CompiledExecutedTestCaseResultFailure {
   public  CompiledExecutedTestCaseResultFailureImpl(final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, final Instant executionStarted, final Instant executionFinished, final Instant processingFinished) {
        super(processingStarted, compilationStarted, compilationFinished, executionStarted, executionFinished, processingFinished);
    }
}
