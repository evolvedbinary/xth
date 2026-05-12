package com.evolvedbinary.xth.tsom.result.compiled;

import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.compiled.AbstractCompiledTestCaseResult;

import java.time.Instant;

public sealed interface CompiledTestCaseResult extends TestCaseResult permits AbstractCompiledTestCaseResult, CompiledTestCaseResultPass, CompiledTestCaseResultFailure, CompiledTestCaseResultError, CompiledExecutedTestCaseResult {

    /**
     * The time that compilation of the test case stated.
     *
     * @return the time that compilation started.
     */
    Instant getCompilationStarted();

    /**
     * The time that compilation of the test case finished
     *
     * @return the time that compilation finished.
     */
    Instant getCompilationFinished();
}
