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
package com.evolvedbinary.xth.connector.api;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import net.jcip.annotations.ThreadSafe;

import java.util.List;

/**
 * Interface for a Connector that can execute
 * test cases with a specific implementation,
 *
 * Implementations must be thread safe.
 */
@ThreadSafe
public interface Connector<T extends TestCaseExecutionContext> {

    /**
     * Get the name of the connector.
     *
     * Should be a short but humane name.
     */
    default String getConnectorName() {
        return getClass().getSimpleName();
    }

    /**
     * Get the name of the implementation that this connects to.
     *
     * @return the implementation name.
     */
    String getImplementationName() throws ConnectorException;

    /**
     * Get the version of the implementation that this connects to.
     *
     * @return the implementation version.
     */
    String getImplementationVersion() throws ConnectorException;

    /**
     * Checks whether the implementation supports the required dependencies.
     *
     * @param dependencies the required dependencies to check for.
     *
     * @return An empty list if all dependencies are supported, otherwise a list of the unsupported dependencies
     */
    List<Dependency<?>> supports(final List<Dependency<?>> dependencies);

    /**
     * Performs any upfront preparation for a test case before it is executed.
     *
     * For example, compilation could be performed at this stage.
     *
     * @param testSet the Test Set.
     * @param testCase the Test Case.
     *
     * @return An Execution Context for the Test Case.
     *
     * @throws ConnectorException if an error that is outside the bound of the test case occurs.
     */
    T prepareTestCaseForExecution(TestSet testSet, TestCase testCase) throws ConnectorException;

    /**
     * Execute a test case.
     *
     * @param testCaseExecutionContext the execution context produced by {@link #prepareTestCaseForExecution(TestSet, TestCase)}.
     *
     * @return The result of executing the test case.
     */
    TestCaseResult executeTestCase(T testCaseExecutionContext);
}
