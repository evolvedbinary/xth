package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResultFailure;

public sealed interface TestCaseResultFailure extends TestCaseResult permits CompiledTestCaseResultFailure, ExecutedTestCaseResultFailure, CompiledExecutedTestCaseResultFailure {
}
