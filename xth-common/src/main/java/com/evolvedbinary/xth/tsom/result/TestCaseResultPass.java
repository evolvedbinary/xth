package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.compiled.CompiledTestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.compiled.executed.CompiledExecutedTestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.executed.ExecutedTestCaseResultPass;

public sealed interface TestCaseResultPass extends TestCaseResult permits CompiledTestCaseResultPass, ExecutedTestCaseResultPass, CompiledExecutedTestCaseResultPass {
}
