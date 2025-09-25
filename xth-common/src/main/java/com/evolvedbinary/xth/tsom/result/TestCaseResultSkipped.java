package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultSkippedImpl;

public sealed interface TestCaseResultSkipped extends TestCaseResult permits TestCaseResultSkippedImpl {
}
