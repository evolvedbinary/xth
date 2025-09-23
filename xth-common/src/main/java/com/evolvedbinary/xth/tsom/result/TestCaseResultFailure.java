package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultFailureImpl;

public sealed interface TestCaseResultFailure extends TestCaseResult permits TestCaseResultFailureImpl {
}
