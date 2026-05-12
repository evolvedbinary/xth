package com.evolvedbinary.xth.tsom.result.compiled.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResultError;
import com.evolvedbinary.xth.tsom.result.impl.compiled.executed.CompiledExecutedTestCaseResultErrorImpl;

public sealed interface CompiledExecutedTestCaseResultError extends CompiledExecutedTestCaseResult, TestCaseResultError permits CompiledExecutedTestCaseResultErrorImpl {
}
