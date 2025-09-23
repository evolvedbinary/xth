package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultErrorImpl;

public sealed interface TestCaseResultError extends TestCaseResult permits TestCaseResultErrorImpl {
}
