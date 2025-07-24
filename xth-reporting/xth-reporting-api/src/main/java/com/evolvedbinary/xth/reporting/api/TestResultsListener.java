/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
package com.evolvedbinary.xth.reporting.api;

import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import net.jcip.annotations.ThreadSafe;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of this interface listen for results
 * of tests that are being executed.
 *
 * Implementations must ensure that they are thread-safe
 * as the event methods of this interface may be called
 * from multiple threads.
 */
@ThreadSafe
public interface TestResultsListener extends AutoCloseable {


    /**
     * Called when execution of a suite of test sets starts.
     *
     * @param testSuite the details of the test suite.
     * @param timestamp the time at which execution started.
     */
    void testSuiteStarted(TestSuite testSuite, Instant timestamp);

    /**
     * Called when execution of a suite of test sets finishes.
     *
     * @param testSuite the details of the test suite.
     * @param timestamp the time at which execution finished.
     */
    void testSuiteFinished(TestSuite testSuite, Instant timestamp);

    /**
     * Called when execution of a test set is excluded.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSetId the id of the test set.
     * @param testSetFile the path to the test set file.
     * @param testSetName the name of the test set.
     * @param timestamp the time at which skipping occurred.
     */
    void testSetExcluded(TestSuite testSuite, UUID testSetId, Path testSetFile, String testSetName, Instant timestamp);

    /**
     * Called when execution of a test set is skipped.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSetId the id of the test set.
     * @param testSetFile the path to the test set file.
     * @param testSetName the name of the test set.
     * @param timestamp the time at which skipping occurred.
     */
    void testSetSkipped(TestSuite testSuite, UUID testSetId, Path testSetFile, String testSetName, Instant timestamp);


    /**
     * Called when execution of a set of test cases starts.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSet the details of the test set.
     * @param timestamp the time at which execution started.
     */
    void testSetStarted(TestSuite testSuite, TestSet testSet, Instant timestamp);

    /**
     * Called when execution of a set of test cases finishes.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSet the details of the test set.
     * @param timestamp the time at which execution finished.
     */
    void testSetFinished(TestSuite testSuite, TestSet testSet, Instant timestamp);

    /**
     * Called when execution of a test case is excluded.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSet the details of the (containing) test set.
     * @param testCaseId the unique id of the test case instance.
     * @param testCaseName the name of the test case.
     * @param timestamp the time at which exclusion occurred.
     */
    void testCaseExcluded(TestSuite testSuite, TestSet testSet, UUID testCaseId, String testCaseName, Instant timestamp);

    /**
     * Called when execution of a test case is skipped.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSet the details of the (containing) test set.
     * @param testCaseId the unique id of the test case instance.
     * @param testCaseName the name of the test case.
     * @param timestamp the time at which skipping occurred.
     */
    void testCaseSkipped(TestSuite testSuite, TestSet testSet, UUID testCaseId, String testCaseName, Instant timestamp);

    /**
     * Called when execution of a test case starts.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSet the details of the (containing) test set.
     * @param testCase the details of the test case.
     * @param timestamp the time at which execution started.
     */
    void testCaseStarted(TestSuite testSuite, TestSet testSet, TestCase testCase, Instant timestamp);

    /**
     * Called when execution of a test case finishes.
     *
     * @param testSuite the details of the (containing) test suite.
     * @param testSet the details of the (containing) test set.
     * @param testCase the details of the test case.
     * @param timestamp the time at which execution finished.
     * @param testCaseResult the result from executing the test case.
     */
    void testCaseFinished(TestSuite testSuite, TestSet testSet, TestCase testCase, Instant timestamp, TestCaseResult testCaseResult);

    /**
     * Close the TestResults listener.
     */
    @Override
    void close();
}
