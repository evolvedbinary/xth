package com.evolvedbinary.xth.tsom.result.compiled;

import com.evolvedbinary.xth.tsom.result.TestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.impl.compiled.CompiledTestCaseResultPassImpl;;

public sealed interface CompiledTestCaseResultPass extends CompiledTestCaseResult, TestCaseResultPass permits CompiledTestCaseResultPassImpl {
}
