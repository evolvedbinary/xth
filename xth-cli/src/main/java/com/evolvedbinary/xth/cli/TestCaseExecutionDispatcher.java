package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.parser.api.ParserEventListener;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.evolvedbinary.xth.util.MapUtil.*;
import static com.evolvedbinary.xth.util.SetUtil.safeAdd;

@NotThreadSafe
public class TestCaseExecutionDispatcher implements ParserEventListener {

    @GuardedBy("connectorInitialized")
    private final Connector connector;
    private final AtomicBoolean connectorInitialized = new AtomicBoolean();

    private final StructuredTaskScope<Void, Void> stsTestCaseExecutor;

    private @Nullable Set<TestResultsListener> listeners;

    private @Nullable Map<UUID, CatalogInfo> tsCatalogs;
    private @Nullable Map<UUID, List<EnvironmentDefinition>> tsGlobalEnvironments;
    private @Nullable Map<UUID, Map<UUID, TestSetInfo>> tsTestSets;

    public TestCaseExecutionDispatcher(final Connector connector, final StructuredTaskScope<Void, Void> stsTestCaseExecutor) {
        this.connector = connector;
        this.stsTestCaseExecutor = stsTestCaseExecutor;
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
    public void startParseCatalog(final UUID parseId, final Path catalogFile, final SpecificationVersion defaultSpecification) {
        tsCatalogs = safePut(tsCatalogs, parseId, new CatalogInfo(catalogFile, defaultSpecification));
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
        final SpecificationVersion defaultSpecification = catalogInfo.defaultSpecification;
        final List<EnvironmentDefinition> globalEnvironments = tsGlobalEnvironments.remove(parseId);
        stsTestCaseExecutor.fork(new Callable() {
            @Override
            public Void call() throws ConnectorException {
                connector.initialize(baseUri, defaultSpecification, globalEnvironments);
                connectorInitialized.set(true);
                return null;
            }
        });
    }

    @Override
    public void startParseTestSets(final UUID parseId) {
        // no-op
    }

    @Override
    public void startParseTestSet(final UUID parseId, final UUID testSetId, final TestSet testSet) {
        final TestSetInfo testSetInfo = new TestSetInfo(testSet);
        tsTestSets = safePutMap(tsTestSets, parseId, testSetId, testSetInfo);

        // start a thread to calculate whether the dependencies of this Test Set are met
        stsTestCaseExecutor.fork(new Callable() {
            public Void call() throws ConnectorException {

                // make sure the connector is initialized before we proceed
                waitUntil(connectorInitialized::get, initialized -> initialized == true);

                // does the connector support the dependencies required by this Test Set?
                final List<Dependency> testSetUnmetDependencies = connector.supports(testSet.getDependencies());
                // TODO(AR) pass on the details of the testSetUnmetDependencies to the TestCaseResultSkipped class
                testSetInfo.dependenciesMet.set(testSetUnmetDependencies.isEmpty() ? DependenciesCheckState.MET : DependenciesCheckState.NOT_MET);
                return null;
            }
        });
    }

    @Override
    public void testCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final TestCase testCase) {
        final TestSetInfo testSetInfo = tsTestSets.get(parseId).get(testSetId);

        stsTestCaseExecutor.fork(new Callable() {
            @Override
            public Void call() throws ConnectorException {

                // make sure the dependency check of the Test Set has complete
                final DependenciesCheckState testSetDependenciesMet = waitUntil(testSetInfo.dependenciesMet::get, state -> state != DependenciesCheckState.UNKNOWN);

                // Are the dependencies of the Test Set met?
                if (testSetDependenciesMet != DependenciesCheckState.MET) {
                    // dependencies for the Test Set are not met, we can skip all Test Cases in this Test Set
                    emitTestResult(testSetInfo.testSet, testCase, new TestCaseResultSkippedImpl(String.format("Dependencies for the Test Set: %s have not been met by the Connector", testSetInfo.testSet.getName())));
                    return null;
                }

                // Are the dependencies of the Test Case met?
                final List<Dependency> testCaseUnmetDependencies = connector.supports(testCase.getDependencies());
                if (!testCaseUnmetDependencies.isEmpty()) {
                    // dependencies for the Test Case are not met, we can skip this Test Case
                    // TODO(AR) pass on the details of the testCaseUnmetDependencies to the TestCaseResultSkipped class
                    emitTestResult(testSetInfo.testSet, testCase, new TestCaseResultSkippedImpl(String.format("Dependencies for the Test Case: %s  have not been met by the Connector", testCase.getName())));
                    return null;
                }

                // execute the test case
                final TestCaseResult testCaseResult = connector.executeTestCase(testSetInfo.testSet, testCase);

                // emit the result
                emitTestResult(testSetInfo.testSet, testCase, testCaseResult);

                return null;
            }
        });
    }

    private <T> T waitUntil(final Supplier<T> source, final Predicate<T> predicate) throws ConnectorException {
        // timeout settings for awaiting initialisation of the connector
        final long maxWait = 60 * 1024;     // 60 Seconds
        final long waitPerLoop = 200;       // 200 Milliseconds
        long waited = 0;

        // NOTE(AR) make sure the connector is initialized first, wait if necessary
        while (true) {

            final T value = source.get();
            if (predicate.test(value)) {
                return value;
            }

            // check if we have exceeded a timeout
            if (waited > maxWait) {
                throw new ConnectorException(String.format("Max wait: %i exceeded when whilst waiting for the connector to be initialized", maxWait));
            }

            try {
                Thread.sleep(waitPerLoop);
            } catch (final InterruptedException e) {
                // restore thread's interrupted flag
                Thread.interrupted();
                throw new ConnectorException(String.format("Thread interrupted whilst waiting for the connector to be initialized: %s", e.getMessage()), e);
            }

            waited += waitPerLoop;
        }
    }

    private void emitTestResult(final TestSet testSet, final TestCase testCase, final TestCaseResult testCaseResult) {
        if (listeners != null) {
            final Iterator<TestResultsListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final TestResultsListener testResultsListener = iterator.next();
                testResultsListener.result(testSet, testCase, testCaseResult);
            }
        }
    }

    @Override
    public void endParseTestSet(final UUID parseId, final UUID testSetId) {
        tsTestSets.get(parseId).remove(testSetId);
    }

    @Override
    public void endParseTestSets(final UUID parseId) {
        tsTestSets.remove(parseId);
    }

    @Override
    public void endParseCatalog(final UUID parseId) {
        tsCatalogs.remove(parseId);
    }

    private static class CatalogInfo {
        final Path catalogFile;
        final SpecificationVersion defaultSpecification;

        private CatalogInfo(final Path catalogFile, final SpecificationVersion defaultSpecification) {
            this.catalogFile = catalogFile;
            this.defaultSpecification = defaultSpecification;
        }
    }

    private static class TestSetInfo {
        private final TestSet testSet;
        private final AtomicReference<DependenciesCheckState> dependenciesMet = new AtomicReference<>(DependenciesCheckState.UNKNOWN);

        private TestSetInfo(final TestSet testSet) {
            this.testSet = testSet;
        }
    }

    private enum DependenciesCheckState {
        UNKNOWN,
        MET,
        NOT_MET;
    }
}
