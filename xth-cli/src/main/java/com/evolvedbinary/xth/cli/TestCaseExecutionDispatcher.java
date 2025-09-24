package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.parser.api.ParserEventListener;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
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

    private @Nullable Map<UUID, Path> tsCatalogs;
    private @Nullable Map<UUID, List<EnvironmentDefinition>> tsGlobalEnvironments;
    private @Nullable Map<UUID, Map<UUID, TestSet>> tsTestSets;

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
    public void startParseCatalog(final UUID parseId, final Path catalogFile) {
        tsCatalogs = safePut(tsCatalogs, parseId, catalogFile);
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
        final Path baseUri = tsCatalogs.get(parseId).getParent();
        final List<EnvironmentDefinition> globalEnvironments = tsGlobalEnvironments.remove(parseId);
        stsTestCaseExecutor.fork(new Callable() {
            @Override
            public Void call() throws ConnectorException {
                connector.initialize(baseUri, globalEnvironments);
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
        tsTestSets = safePutMap(tsTestSets, parseId, testSetId, testSet);
    }

    @Override
    public void testCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final TestCase testCase) {
        final TestSet testSet = tsTestSets.get(parseId).get(testSetId);
        stsTestCaseExecutor.fork(new Callable() {
            @Override
            public Void call() throws ConnectorException {

                // timeout settings for awaiting initialisation of the connector
                final long maxWait = 60 * 1024;     // 60 Seconds
                final long waitPerLoop = 200;       // 200 Milliseconds
                long waited = 0;

                // NOTE(AR) make sure the connector is initialized first, wait if necessary
                while (connectorInitialized.get() == false) {
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

                // execute the test case
                final TestCaseResult testCaseResult = connector.executeTestCase(testSet, testCase);

                // emit the result
                emitTestResult(testSet, testCase, testCaseResult);

                return null;
            }
        });
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
}
