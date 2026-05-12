package com.evolvedbinary.xth.tsom.result.compiled;

import com.evolvedbinary.xth.tsom.result.TestCaseResultError;
import com.evolvedbinary.xth.tsom.result.impl.compiled.CompiledTestCaseResultErrorImpl;;

public sealed interface CompiledTestCaseResultError extends CompiledTestCaseResult, TestCaseResultError permits CompiledTestCaseResultErrorImpl {
}
