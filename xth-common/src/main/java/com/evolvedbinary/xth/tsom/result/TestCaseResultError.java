package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResultError;
import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResultError;
import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResultError;

public sealed interface TestCaseResultError extends TestCaseResult permits CompiledTestCaseResultError, ExecutedTestCaseResultError, CompiledExecutedTestCaseResultError {
}
