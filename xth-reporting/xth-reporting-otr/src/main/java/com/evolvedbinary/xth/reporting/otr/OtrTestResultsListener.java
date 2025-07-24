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
package com.evolvedbinary.xth.reporting.otr;

import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.reporting.api.ReporterConfiguration;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.TestCaseResultError;
import com.evolvedbinary.xth.tsom.result.TestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.TestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.TestCaseResultSkipped;
import net.jcip.annotations.ThreadSafe;
import org.jspecify.annotations.Nullable;
import org.opentest4j.reporting.events.api.DocumentWriter;
import org.opentest4j.reporting.events.api.NamespaceRegistry;
import org.opentest4j.reporting.events.core.CoreFactory;
import org.opentest4j.reporting.events.core.Result;
import org.opentest4j.reporting.events.root.Events;
import org.opentest4j.reporting.events.root.Finished;
import org.opentest4j.reporting.schema.Namespace;
import org.opentest4j.reporting.tooling.core.htmlreport.DefaultHtmlReportWriter;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.opentest4j.reporting.events.core.CoreFactory.cpuCores;
import static org.opentest4j.reporting.events.core.CoreFactory.hostName;
import static org.opentest4j.reporting.events.core.CoreFactory.infrastructure;
import static org.opentest4j.reporting.events.core.CoreFactory.operatingSystem;
import static org.opentest4j.reporting.events.root.RootFactory.finished;
import static org.opentest4j.reporting.events.root.RootFactory.started;

@ThreadSafe
public class OtrTestResultsListener implements TestResultsListener {

    private final Path xmlEventsOutputFile;
    private volatile @Nullable DocumentWriter<Events> documentWriter;
    private final Path htmlReportOutputFile;
    private @Nullable final PrintStream stdOut;
    private @Nullable final PrintStream stdErr;

    private OtrTestResultsListener(final Path xmlEventsOutputFile, final DocumentWriter<Events> documentWriter, final Path htmlReportOutputFile, final PrintStream stdOut, final PrintStream stdErr) {
        this.xmlEventsOutputFile = xmlEventsOutputFile;
        this.documentWriter = documentWriter;
        this.htmlReportOutputFile = htmlReportOutputFile;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public static OtrTestResultsListener open(final ReporterConfiguration reporterConfiguration) throws IOException {
        final NamespaceRegistry namespaceRegistry = NamespaceRegistry.builder(Namespace.REPORTING_CORE)
                .add("e", Namespace.REPORTING_EVENTS)
                .add("java", Namespace.REPORTING_JAVA)
                .build();

        final Path xmlEventsOutputFile = reporterConfiguration.outputDirectory().resolve("otr.events.xml");   // TODO(AR) need to make this configurable so there is one per-run - or better yet - one results folder for each run (configure in cli-main)
        final Path htmlReportOutputFile = reporterConfiguration.outputDirectory().resolve("otr.html");   // TODO(AR) need to make this configurable so there is one per-run - or better yet - one results folder for each run (configure in cli-main)

        try {
            final DocumentWriter<Events> documentWriter = new ThreadSafeDefaultDocumentWriter<>(Events.createDocumentWriter(namespaceRegistry, xmlEventsOutputFile));

            final String hostName = InetAddress.getLocalHost().getHostName();
            documentWriter.append(infrastructure(), infrastructure -> infrastructure
                            .append(hostName(hostName))
                            .append(operatingSystem(System.getProperty("os.name")))
                            .append(cpuCores(Runtime.getRuntime().availableProcessors()))
//                .append(metadata(), metadata -> metadata.withAttribute(key, value))  // TODO(AR) figure out how to add metadata
            );

            return new OtrTestResultsListener(xmlEventsOutputFile, documentWriter, htmlReportOutputFile, reporterConfiguration.stdOut(), reporterConfiguration.stdErr());

        } catch (final Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void testSuiteStarted(final TestSuite testSuite, final Instant timestamp) {
        throwIfClosed();
        documentWriter.append(started(testSuite.getTestSuiteInstanceId().toString(), timestamp, testSuite.getName() + " " + testSuite.getVersion()));
    }

    @Override
    public void testSuiteFinished(final TestSuite testSuite, final Instant timestamp) {
        throwIfClosed();
        documentWriter.append(finished(testSuite.getTestSuiteInstanceId().toString(), timestamp), finished -> finished.append(CoreFactory.result(Result.Status.SUCCESSFUL)));
    }

    @Override
    public void testSetExcluded(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        throwIfClosed();
        // TODO(AR) add metadata about Excluded TestSets to the OTR report - or could we use Result.Status.ABORTED?
    }

    @Override
    public void testSetSkipped(final TestSuite testSuite, final UUID testSetId, final Path testSetFile, final String testSetName, final Instant timestamp) {
        throwIfClosed();
        documentWriter.append(started(testSetId.toString(), timestamp, testSetName), started -> started.withParentId(testSuite.getTestSuiteInstanceId().toString()));
        documentWriter.append(finished(testSetId.toString(), timestamp), finished -> finished.append(CoreFactory.result(Result.Status.SKIPPED)));
    }

    @Override
    public void testSetStarted(final TestSuite testSuite, final TestSet testSet, final Instant timestamp) {
        throwIfClosed();
        documentWriter.append(started(testSet.getTestSetInstanceId().toString(), timestamp, testSet.getName()), started -> started.withParentId(testSuite.getTestSuiteInstanceId().toString()));
    }

    @Override
    public void testSetFinished(final TestSuite testSuite, final TestSet testSet, final Instant timestamp) {
        throwIfClosed();
        documentWriter.append(finished(testSet.getTestSetInstanceId().toString(), timestamp), finished -> finished.append(CoreFactory.result(Result.Status.SUCCESSFUL)));
    }

    @Override
    public void testCaseExcluded(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        // TODO(AR) add metadata about Excluded TestCases to the OTR report - or could we use Result.Status.ABORTED?
    }

    @Override
    public void testCaseSkipped(final TestSuite testSuite, final TestSet testSet, final UUID testCaseId, final String testCaseName, final Instant timestamp) {
        documentWriter.append(started(testCaseId.toString(), timestamp, testCaseName), started -> started.withParentId(testSet.getTestSetInstanceId().toString()));
        documentWriter.append(finished(testCaseId.toString(), timestamp), finished -> finished.append(CoreFactory.result(Result.Status.SKIPPED)));

    }

    @Override
    public void testCaseStarted(final TestSuite testSuite, final TestSet testSet, final TestCase testCase, final Instant timestamp) {
        documentWriter.append(started(testCase.getTestCaseInstanceId().toString(), timestamp, testCase.getName()), started -> started.withParentId(testSet.getTestSetInstanceId().toString()));
    }

    @Override
    public void testCaseFinished(final TestSuite testSuite, final TestSet testSet, final TestCase testCase, final Instant timestamp, final TestCaseResult testCaseResult) {
        throwIfClosed();

        // TODO(AR) attach metadata about process time, compilation time, and execution time

        final Consumer<Finished> finishedDataProvider = switch (testCaseResult) {
            case TestCaseResultSkipped testCaseResultSkipped ->
                    finished -> finished
                            .append(CoreFactory.result(Result.Status.SKIPPED));
            // TODO(AR) add the reason
//                          .append(CoreFactory.reason(testCaseResultSkipped.getReason()))

            case TestCaseResultFailure testCaseResultFailure ->
                    finished -> finished.append(CoreFactory.result(Result.Status.FAILED));

            case TestCaseResultError testCaseResultError ->
                    finished -> finished.append(CoreFactory.result(Result.Status.ERRORED));

            case TestCaseResultPass testCaseResultPass ->
                    finished -> finished.append(CoreFactory.result(Result.Status.SUCCESSFUL));
        };

        documentWriter.append(finished(testCase.getTestCaseInstanceId().toString(), timestamp), finishedDataProvider);
    }

    private void throwIfClosed() {
        if (documentWriter == null) {
            throw new IllegalStateException("OtrTestResultsListener has already been closed");
        }
    }

    @Override
    public void close() {
        if (documentWriter == null) {
            return;
        }

        try {
            try {
                documentWriter.close();
                if (stdOut != null) {
                    stdOut.println(String.format("Wrote OTR XML Results file to: %s", xmlEventsOutputFile.toAbsolutePath()));
                }
            } catch (final IOException e) {
                if (stdErr != null) {
                    e.printStackTrace();
                }
                return;
            }

            try {
                // TODO(AR) make this an optional configuration option?
                new DefaultHtmlReportWriter().writeHtmlReport(List.of(xmlEventsOutputFile), htmlReportOutputFile);
                if (stdOut != null) {
                    stdOut.println(String.format("Wrote OTR HTML Report file to: %s", htmlReportOutputFile.toAbsolutePath()));
                }
            } catch (final Exception e) {
                if (stdErr != null) {
                    e.printStackTrace();
                }
                return;
            }

        } finally {
            documentWriter = null;
        }
    }
}
