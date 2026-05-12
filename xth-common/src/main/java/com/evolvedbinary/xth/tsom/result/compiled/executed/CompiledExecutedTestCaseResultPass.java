package com.evolvedbinary.xth.tsom.result.compiled.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.impl.compiled.executed.CompiledExecutedTestCaseResultPassImpl;

public sealed interface CompiledExecutedTestCaseResultPass extends CompiledExecutedTestCaseResult, TestCaseResultPass permits CompiledExecutedTestCaseResultPassImpl {
}
