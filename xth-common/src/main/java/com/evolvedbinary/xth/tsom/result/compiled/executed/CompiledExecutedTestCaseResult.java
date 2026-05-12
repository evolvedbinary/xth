package com.evolvedbinary.xth.tsom.result.compiled.executed;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResult;
import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.compiled.executed.AbstractCompiledExecutedTestCaseResult;

public sealed interface CompiledExecutedTestCaseResult extends CompiledTestCaseResult, ExecutedTestCaseResult permits AbstractCompiledExecutedTestCaseResult, CompiledExecutedTestCaseResultPass, CompiledExecutedTestCaseResultFailure, CompiledExecutedTestCaseResultError {
}
