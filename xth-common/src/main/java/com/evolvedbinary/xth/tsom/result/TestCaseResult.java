package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResult;
import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.AbstractTestCaseResult;

import java.time.Instant;

public sealed interface TestCaseResult permits CompiledTestCaseResult, ExecutedTestCaseResult, AbstractTestCaseResult, TestCaseResultPass, TestCaseResultFailure, TestCaseResultError, TestCaseResultSkipped {

    /**
     * The time that processing (by the connected processor) of the test case stated.
     *
     * @return the time that processing started.
     */
    Instant getProcessingStarted();

    /**
     * The time that processing (by the connected processor) of the test case finished.
     *
     * @return the time that processing finished.
     */
    Instant getProcessingFinished();
}
