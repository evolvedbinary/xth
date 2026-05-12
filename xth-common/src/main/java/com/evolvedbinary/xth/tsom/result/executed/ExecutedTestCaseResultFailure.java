package com.evolvedbinary.xth.tsom.result.executed;

import com.evolvedbinary.xth.tsom.result.TestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.impl.executed.ExecutedTestCaseResultFailureImpl;

public sealed interface ExecutedTestCaseResultFailure extends ExecutedTestCaseResult, TestCaseResultFailure permits ExecutedTestCaseResultFailureImpl {
}
