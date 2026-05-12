package com.evolvedbinary.xth.tsom.result.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.impl.executed.ExecutedTestCaseResultPassImpl;

public sealed interface ExecutedTestCaseResultPass extends ExecutedTestCaseResult, TestCaseResultPass permits ExecutedTestCaseResultPassImpl {
}
