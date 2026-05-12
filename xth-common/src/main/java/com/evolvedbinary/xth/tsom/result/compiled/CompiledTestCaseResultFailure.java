package com.evolvedbinary.xth.tsom.result.compiled;

import com.evolvedbinary.xth.tsom.result.TestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.impl.compiled.CompiledTestCaseResultFailureImpl;;

public sealed interface CompiledTestCaseResultFailure extends CompiledTestCaseResult, TestCaseResultFailure permits CompiledTestCaseResultFailureImpl {
}
