package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultPassImpl;

public sealed interface TestCaseResultPass extends TestCaseResult permits TestCaseResultPassImpl {
}
