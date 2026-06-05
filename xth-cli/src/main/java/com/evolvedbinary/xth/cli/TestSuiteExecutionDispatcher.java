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
package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.connector.api.ConnectorFactory;
import com.evolvedbinary.xth.connector.api.ConnectorFactoryException;
import com.evolvedbinary.xth.connector.api.TestCaseExecutionContext;
import com.evolvedbinary.xth.parser.api.ParserEventListener;
import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.PrepareTestCaseResultErrorImpl;
import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultSkippedImpl;
import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.evolvedbinary.xth.util.MapUtil.*;
import static com.evolvedbinary.xth.util.SetUtil.safeAdd;

@NotThreadSafe
public class TestSuiteExecutionDispatcher implements ParserEventListener {

    private final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory;
    private final CompletableFuture<Connector<TestCaseExecutionContext>> futConnector = new CompletableFuture<>();

    private final StructuredTaskScope<Void, Void> stsTestSuiteExecutor;

    private @Nullable Set<TestResultsListener> listeners;

    private @Nullable Map<UUID, CatalogInfo> tsCatalogs;
    private @Nullable Map<UUID, List<EnvironmentDefinition>> tsGlobalEnvironments;
    private @Nullable Map<UUID, TestSuite> tsTestSuites;
    private @Nullable Map<UUID, Map<UUID, TestSetInfo>> tsTestSets;

    public TestSuiteExecutionDispatcher(final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory, final StructuredTaskScope<Void, Void> stsTestSuiteExecutor) {
        this.connectorFactory = connectorFactory;
        this.stsTestSuiteExecutor = stsTestSuiteExecutor;
    }

    public void addTestResultsListener(final TestResultsListener testResultsListener) {
        listeners = safeAdd(listeners, testResultsListener, LinkedHashSet::new);
    }

    public void removeTestResultsListener(final TestResultsListener testResultsListener) {
        if (listeners != null) {
            listeners.remove(testResultsListener);
        }
    }

    @Override
    public void startParseCatalog(final UUID parseId, final Path catalogFile, final EnumSet<SpecificationVersion> defaultSpecifications) {
        tsCatalogs = safePut(tsCatalogs, parseId, new CatalogInfo(catalogFile, defaultSpecifications));
    }

    @Override
    public void startParseCatalogEnvironments(final UUID parseId) {
        // no-op
    }

    @Override
    public void catalogEnvironment(final UUID parseId, final EnvironmentDefinition environment) {
        tsGlobalEnvironments = safePutList(tsGlobalEnvironments, parseId, environment);
    }

    @Override
    public void endParseCatalogEnvironments(final UUID parseId) {
        final CatalogInfo catalogInfo = tsCatalogs.get(parseId);
        final Path baseUri = catalogInfo.catalogFile.getParent();
        final EnumSet<SpecificationVersion> defaultSpecifications = catalogInfo.defaultSpecifications;
        final List<EnvironmentDefinition> globalEnvironments = tsGlobalEnvironments.remove(parseId);
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() throws ConnectorFactoryException {
                try {
                    final Connector connector = connectorFactory.newConnector(baseUri, defaultSpecifications, globalEnvironments);
                    futConnector.complete(connector);
                    return null;
                } catch (final ConnectorFactoryException t) {
                    futConnector.completeExceptionally(t);
                    throw t;
                }
            }
        });
    }

    @Override
    public void startParseTestSets(final UUID parseId, final TestSuite testSuite) {
        tsTestSuites = safePut(tsTestSuites, parseId, testSuite);

        final Instant testSuiteStarted = Instant.now();

        // start a thread to notify test result listeners that we have started executing a test suite
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestSuiteStarted(testSuite, testSuiteStarted);
                return null;
            }
        });
    }

    @Override
    public void excludedTestSet(final UUID parseId, final UUID testSetId, final Path testSetFile, final String testSetName) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final Instant testSetExcluded = Instant.now();

        // start a thread to notify test result listeners that we have excluded executing a test set
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestSetExcluded(testSuite, testSetId, testSetFile, testSetName, testSetExcluded);
                return null;
            }
        });
    }

    @Override
    public void skippedTestSet(final UUID parseId, final UUID testSetId, final Path testSetFile, final String testSetName) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final Instant testSetSkipped = Instant.now();

        // start a thread to notify test result listeners that we have skipped executing a test set
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestSetSkipped(testSuite, testSetId, testSetFile, testSetName, testSetSkipped);
                return null;
            }
        });
    }

    @Override
    public void startParseTestSet(final UUID parseId, final UUID testSetId, final TestSet testSet) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final TestSetInfo testSetInfo = new TestSetInfo(testSet);
        tsTestSets = safePutMap(tsTestSets, parseId, testSetId, testSetInfo);

        final Instant testSetStarted = Instant.now();

        // start a thread to notify test result listeners that we have started executing a test set
//        stsTestSuiteExecutor.fork(new Callable<Void>() {
//            @Override
//            public Void call() {
                emitTestSetStarted(testSuite, testSet, testSetStarted);
//                return null;
//            }
//        });

        // start a thread to calculate whether the dependencies of this Test Set are met
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() throws ConnectorException {
                try {
                    // make sure the connector is initialized before we proceed
                    final Connector<TestCaseExecutionContext> connector = getConnector();

                    // does the connector support the dependencies required by this Test Set?
                    final List<Dependency<?>> testSetUnmetDependencies = connector.supports(testSet.getDependencies());
                    testSetInfo.unmetDependencies.complete(testSetUnmetDependencies);
                    return null;
                } catch (final ConnectorException e) {
                    testSetInfo.unmetDependencies.completeExceptionally(e);
                    throw e;
                }
            }
        });
    }

    @Override
    public void excludedTestCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final String testCaseName) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final TestSetInfo testSetInfo = tsTestSets.get(parseId).get(testSetId);
        final Instant testCaseExcluded = Instant.now();

        // start a thread to notify test result listeners that we have excluded executing a test case
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestCaseExcluded(testSuite, testSetInfo.testSet, testCaseId, testCaseName, testCaseExcluded);
                return null;
            }
        });
    }

    @Override
    public void skippedTestCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final String testCaseName) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final TestSetInfo testSetInfo = tsTestSets.get(parseId).get(testSetId);
        final Instant testCaseSkipped = Instant.now();

        // start a thread to notify test result listeners that we have skipped executing a test case
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestCaseSkipped(testSuite, testSetInfo.testSet, testCaseId, testCaseName, testCaseSkipped);
                return null;
            }
        });
    }

    @Override
    public void testCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final TestCase testCase) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final TestSetInfo testSetInfo = tsTestSets.get(parseId).get(testSetId);

        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() throws ConnectorException {

                final Instant testCaseStarted = Instant.now();
                emitTestCaseStarted(testSuite, testSetInfo.testSet, testCase, testCaseStarted);

                // make sure the dependency check of the Test Set has completed
                final List<Dependency<?>> testSetUnmetDependencies = getTestSetUnmetDependencies(testSetInfo);

                // Are the dependencies of the Test Set met?
                if (!testSetUnmetDependencies.isEmpty()) {
                    // dependencies for the Test Set are not met, we can skip all Test Cases in this Test Set
                    final Instant testCaseFinished = Instant.now();
                    // TODO(AR) pass on the details of the testSetUnmetDependencies to the TestCaseResultSkipped class
                    emitTestCaseFinished(testSuite, testSetInfo.testSet, testCase, testCaseFinished, new TestCaseResultSkippedImpl(testCaseFinished, String.format("Dependencies for the Test Set: %s have not been met by the Connector", testSetInfo.testSet.getName())));
                    return null;
                }

                // Are the dependencies of the Test Case met?
                final Connector<TestCaseExecutionContext> connector = getConnector();
                final List<Dependency<?>> testCaseUnmetDependencies = connector.supports(testCase.getDependencies());
                if (!testCaseUnmetDependencies.isEmpty()) {
                    // dependencies for the Test Case are not met, we can skip this Test Case
                    // TODO(AR) pass on the details of the testCaseUnmetDependencies to the TestCaseResultSkipped class
                    final Instant testCaseFinished = Instant.now();
                    emitTestCaseFinished(testSuite, testSetInfo.testSet, testCase, testCaseFinished, new TestCaseResultSkippedImpl(testCaseFinished, String.format("Dependencies for the Test Case: %s  have not been met by the Connector", testCase.getName())));
                    return null;
                }

                // setup the text case for execution
                final TestCaseExecutionContext testCaseExecutionContext;
                try {
                    testCaseExecutionContext = connector.prepareTestCaseForExecution(testSetInfo.testSet, testCase);
                } catch (final ConnectorException e) {
                    // an exception occurred whilst setting up execution of the test case
                    final Instant testCaseFinished = Instant.now();

                    // TODO(AR) what error to emit here?
                    //throw e; // TODO(AR) probably emit Error result instead of throwing exception
                    final TestCaseResult errorResult = new PrepareTestCaseResultErrorImpl(testCaseStarted, e, testCaseFinished);
                    emitTestCaseFinished(testSuite, testSetInfo.testSet, testCase, testCaseFinished, errorResult);
                    return null;
                }

                // execute the test case
                final TestCaseResult testCaseResult = connector.executeTestCase(testCaseExecutionContext);

                // emit the result
                final Instant testCaseFinished = Instant.now();
                emitTestCaseFinished(testSuite, testSetInfo.testSet, testCase, testCaseFinished, testCaseResult);

                return null;
            }
        });
    }

    private Connector<TestCaseExecutionContext> getConnector() throws ConnectorException {
        try {
            return futConnector.get(60, TimeUnit.SECONDS);

        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new ConnectorException(String.format("Thread interrupted whilst waiting for the connector to be initialized: %s", e.getMessage()), e);

        } catch (final TimeoutException e) {
            throw new ConnectorException(String.format("Max wait: 60 seconds exceeded whilst waiting for the connector to be initialized: %s", e.getMessage()), e);

        } catch (final CancellationException e) {
            throw new ConnectorException(String.format("Cancelled: connector initialization was cancelled: %s", e.getMessage()), e);

        } catch (final ExecutionException e) {
            if (e.getCause() instanceof ConnectorException ce) {
                throw ce;
            }
            throw new ConnectorException(String.format("Initialization error: connector could not be initialized: %s", e.getMessage()), e);
        }
    }

    private static List<Dependency<?>> getTestSetUnmetDependencies(final TestSetInfo testSetInfo) throws ConnectorException {
        try {
            return testSetInfo.unmetDependencies.get(60, TimeUnit.SECONDS);

        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new ConnectorException(String.format("Thread interrupted whilst waiting for the list of unmet dependencies for the test set '%s': %s", testSetInfo.testSet.getName(), e.getMessage()), e);

        } catch (final TimeoutException e) {
            throw new ConnectorException(String.format("Max wait: 60 seconds exceeded whilst waiting for the list of unmet dependencies for the test set '%s': %s", testSetInfo.testSet.getName(), e.getMessage()), e);

        } catch (final CancellationException e) {
            throw new ConnectorException(String.format("Cancelled: unmet dependencies check for test set '%s' was cancelled: %s", testSetInfo.testSet.getName(), e.getMessage()), e);

        } catch (final ExecutionException e) {
            if (e.getCause() instanceof ConnectorException ce) {
                throw ce;
            }
            throw new ConnectorException(String.format("Initialization error: unmet dependencies check for test set '%s' could not be executed: %s", testSetInfo.testSet.getName(), e.getMessage()), e);
        }
    }

    private void emitTestSuiteStarted(final TestSuite testSuite, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testSuiteStarted(testSuite, timestamp);
            }
        }
    }

    private void emitTestSetExcluded(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testSetExcluded(testSuite, testSetId, testSetFile, testSetName, timestamp);
            }
        }
    }

    private void emitTestSetSkipped(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testSetSkipped(testSuite, testSetId, testSetFile, testSetName, timestamp);
            }
        }
    }

    private void emitTestSetStarted(final TestSuite testSuite, final TestSet testSet, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testSetStarted(testSuite, testSet, timestamp);
            }
        }
    }

    private void emitTestCaseExcluded(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testCaseExcluded(testSuite, testSet, testCaseId, testCaseName, timestamp);
            }
        }
    }

    private void emitTestCaseSkipped(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testCaseSkipped(testSuite, testSet, testCaseId, testCaseName, timestamp);
            }
        }
    }

    private void emitTestCaseStarted(final TestSuite testSuite, final TestSet testSet, final TestCase testCase, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testCaseStarted(testSuite, testSet, testCase, timestamp);
            }
        }
    }

    private void emitTestCaseFinished(final TestSuite testSuite, final TestSet testSet, final TestCase testCase, final Instant timestamp, final TestCaseResult testCaseResult) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testCaseFinished(testSuite, testSet, testCase, timestamp, testCaseResult);
            }
        }
    }

    private void emitTestSetFinished(final TestSuite testSuite, final TestSet testSet, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testSetFinished(testSuite, testSet, timestamp);
            }
        }
    }

    private void emitTestSuiteFinished(final TestSuite testSuite, final Instant timestamp) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.testSuiteFinished(testSuite, timestamp);
            }
        }
    }

    @Override
    public void endParseTestSet(final UUID parseId, final UUID testSetId) {
        final TestSuite testSuite = tsTestSuites.get(parseId);
        final TestSetInfo testSetInfo = tsTestSets.get(parseId).remove(testSetId);

        final Instant testSetFinished = Instant.now();

        // start a thread to notify test result listeners that we have finished executing a test set
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestSetFinished(testSuite, testSetInfo.testSet, testSetFinished);
                return null;
            }
        });
    }

    @Override
    public void endParseTestSets(final UUID parseId, final TestSuite testSuite) {
        tsTestSets.remove(parseId);
        tsTestSuites.remove(parseId);

        final Instant testSuiteFinished = Instant.now();

        // start a thread to notify test result listeners that we have finished executing a test suite
        stsTestSuiteExecutor.fork(new Callable<Void>() {
            @Override
            public Void call() {
                emitTestSuiteFinished(testSuite, testSuiteFinished);
                return null;
            }
        });
    }

    @Override
    public void endParseCatalog(final UUID parseId) {
        tsCatalogs.remove(parseId);
    }

    private static class CatalogInfo {
        final Path catalogFile;
        final EnumSet<SpecificationVersion> defaultSpecifications;

        private CatalogInfo(final Path catalogFile, final EnumSet<SpecificationVersion> defaultSpecifications) {
            this.catalogFile = catalogFile;
            this.defaultSpecifications = defaultSpecifications;
        }
    }

    private static class TestSetInfo {
        private final TestSet testSet;
        private final CompletableFuture<List<Dependency<?>>> unmetDependencies = new CompletableFuture<>();

        private TestSetInfo(final TestSet testSet) {
            this.testSet = testSet;
        }
    }
}
