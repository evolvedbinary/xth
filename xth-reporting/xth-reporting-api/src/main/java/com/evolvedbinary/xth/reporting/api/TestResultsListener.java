package com.evolvedbinary.xth.reporting.api;

import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;

/**
 * Implementation of this interface listen for results
 * of tests that are being executed.
 */
public interface TestResultsListener extends Closeable {

    /**
     * Called when execution of a set of test cases starts.
     *
     * @param testSet the details of the test set.
     * @param timestamp the time at which execution started.
     */
    void testSetStarted(TestSet testSet, Instant timestamp);

    /**
     * Called when execution of a set of test cases finishes.
     *
     * @param testSet the details of the test set.
     * @param timestamp the time at which execution finished.
     */
    void testSetFinished(TestSet testSet, Instant timestamp);

    /**
     * Called when execution of a test case starts.
     *
     * @param testSet the details of the (containing) test set.
     * @param testCase the details of the test case.
     * @param timestamp the time at which execution started.
     */
    void testCaseStarted(TestSet testSet, TestCase testCase, Instant timestamp);

    /**
     * Called when execution of a test case finishes.
     *
     * @param testSet the details of the (containing) test set.
     * @param testCase the details of the test case.
     * @param timestamp the time at which execution finished.
     * @param testCaseResult the result from executing the test case.
     */
    void testCaseFinished(TestSet testSet, TestCase testCase, Instant timestamp, TestCaseResult testCaseResult);

    @Override
    default void close() throws IOException {
    }
}