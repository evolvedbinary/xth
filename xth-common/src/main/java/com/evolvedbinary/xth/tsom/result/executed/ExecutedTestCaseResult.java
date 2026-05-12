package com.evolvedbinary.xth.tsom.result.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.executed.AbstractExecutedTestCaseResult;

import java.time.Instant;

public sealed interface ExecutedTestCaseResult extends TestCaseResult permits AbstractExecutedTestCaseResult, CompiledExecutedTestCaseResult, ExecutedTestCaseResultPass, ExecutedTestCaseResultFailure, ExecutedTestCaseResultError {

    /**
     * The time that execution of the test case stated.
     *
     * @return the time that execution started.
     */
    Instant getExecutionStarted();

    /**
     * The time that execution of the test case finished.
     *
     * @return the time that execution finished.
     */
    Instant getExecutionFinished();
}
