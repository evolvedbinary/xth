package com.evolvedbinary.xth.tsom.result.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResultError;
import com.evolvedbinary.xth.tsom.result.impl.executed.ExecutedTestCaseResultErrorImpl;

public sealed interface ExecutedTestCaseResultError extends ExecutedTestCaseResult, TestCaseResultError permits ExecutedTestCaseResultErrorImpl {
}
