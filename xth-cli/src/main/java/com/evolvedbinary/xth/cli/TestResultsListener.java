package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;

public interface TestResultsListener {
    void result(TestSet testSet, TestCase testCase, TestCaseResult testCaseResult);
}
