package com.evolvedbinary.xth.tsom.result;

import com.evolvedbinary.xth.tsom.result.impl.AbstractTestCaseResult;

public sealed interface TestCaseResult permits TestCaseResultPass, TestCaseResultFailure, TestCaseResultError, TestCaseResultSkipped, AbstractTestCaseResult {
    long getExecutionTime();
}
