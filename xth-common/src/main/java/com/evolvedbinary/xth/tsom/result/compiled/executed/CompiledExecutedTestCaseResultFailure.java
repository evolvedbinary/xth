package com.evolvedbinary.xth.tsom.result.compiled.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.impl.compiled.executed.CompiledExecutedTestCaseResultFailureImpl;

public sealed interface CompiledExecutedTestCaseResultFailure extends CompiledExecutedTestCaseResult, TestCaseResultFailure permits CompiledExecutedTestCaseResultFailureImpl {
}
