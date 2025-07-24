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

import com.evolvedbinary.xth.configuration.Configuration;
import com.evolvedbinary.xth.configuration.XthProperties;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.connector.api.ConnectorFactory;
import com.evolvedbinary.xth.connector.api.TestCaseExecutionContext;
import com.evolvedbinary.xth.connector.util.ConnectorFactoryUtil;
import com.evolvedbinary.xth.parser.api.ParserConfiguration;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.parser.util.TestSuiteParserFactory;
import com.evolvedbinary.xth.reporting.api.Reporter;
import com.evolvedbinary.xth.reporting.api.ReporterException;
import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.reporting.api.ReporterConfiguration;
import com.evolvedbinary.xth.reporting.util.ReporterUtil;
import com.evolvedbinary.xth.scm.Git;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

/**
 * Command Line Interface for X Test Harness.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class CliMain {

    private final static boolean DEBUG = "true".equals(System.getProperty("xth.debug"));

    private final static int EXIT_CODE_UNABLE_TO_LOAD_PROPERTIES = 1;
    private final static int EXIT_CODE_INVALID_ARGUMENTS = 2;
    private final static int EXIT_CODE_COULD_NOT_CREATE_CACHE_DIRECTORY = 3;
    private final static int EXIT_CODE_COULD_NOT_CREATE_OUTPUT_DIRECTORY = 4;
    private final static int EXIT_CODE_NO_CONNECTORS = 5;
    private final static int EXIT_CODE_CONNECTOR_ERROR = 6;
    private final static int EXIT_CODE_NO_REPORTERS = 7;
    private final static int EXIT_CODE_GIT_REPO_ERROR = 8;
    private final static int EXIT_CODE_PARSER_PROVIDER_IO_ERROR = 9;
    private final static int EXIT_CODE_NO_SUITABLE_PARSER_AVAILABLE = 10;
    private final static int EXIT_CODE_PARSE_ERROR = 11;
    private final static int EXIT_CODE_PROCESS_INTERRUPTED = 12;
    private final static int EXIT_CODE_OPEN_REPORTER_ERROR = 13;


    public static void main(final String[] args) {
        final PrintStream stdOut = System.out;
        final PrintStream stdErr = System.err;

        final XthProperties xthProperties;
        try {
            xthProperties = XthProperties.getInstance();
        } catch (final IOException e) {
            exit(EXIT_CODE_UNABLE_TO_LOAD_PROPERTIES, e, stdErr);
            return;
        }

        banner(xthProperties, stdOut);

        final Arguments arguments;
        try {
            arguments = parseArguments(args);
        } catch (final IllegalArgumentException e) {
            exit(EXIT_CODE_INVALID_ARGUMENTS, e, stdErr);
            return;
        }

        // TODO(AR) check if we have all necessary arguments to decide what to do - check for any conflicting arguments

        // TODO(AR) report if the config is incomplete
        final Configuration configuration = createConfiguration(xthProperties, arguments);
        try {
            if (!Files.exists(configuration.cacheDirectory())) {
                Files.createDirectories(configuration.cacheDirectory());
            }
        } catch (final IOException e) {
            exit(EXIT_CODE_COULD_NOT_CREATE_CACHE_DIRECTORY, e, stdErr);
        }
        stdOut.println(String.format("Using cache directory: %s", configuration.cacheDirectory()));
        try {
            if (!Files.exists(configuration.outputDirectory())) {
                Files.createDirectories(configuration.outputDirectory());
            }
        } catch (final IOException e) {
            exit(EXIT_CODE_COULD_NOT_CREATE_OUTPUT_DIRECTORY, e, stdErr);
        }
        stdOut.println(String.format("Using output directory: %s", configuration.outputDirectory()));


        // TODO(AR) select connector factories based on user args and capabilities
        final List<ConnectorFactory<? extends TestCaseExecutionContext>> connectorFactories = ConnectorFactoryUtil.connectorFactories();
        if (connectorFactories == null) {
            exit(EXIT_CODE_NO_CONNECTORS, new IllegalStateException("No connectors available"), stdErr);
        }
        for (final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory : connectorFactories) {
            stdOut.println(String.format("Loaded connector factory: %s", connectorFactory.getConnectorFactoryName()));
        }

        // TODO(AR) select reporters based on user args
        final ReporterConfiguration reporterConfiguration = new ReporterConfiguration(
                stdOut,
                stdErr,
                configuration.outputDirectory(),
                configuration.verbose()
        );
        final List<Reporter> reporters = ReporterUtil.reporters(reporterConfiguration);
        if (reporters == null) {
            exit(EXIT_CODE_NO_REPORTERS, new IllegalStateException("No reporters available"), stdErr);
        }
        for (final Reporter reporter : reporters) {
            stdOut.println(String.format("Loaded reporter: %s", reporter.getReporterName()));
        }

        // TODO(AR) also print usage if we are missing args or have an invalid combo of args
        if (arguments.isShowHelp()) {
            usage(xthProperties, stdOut);
            return;
        }

        // TODO(AR) download a test suite via external git
        final Path gitRepoPath;
        try {
            if (!arguments.isOfflineMode()) {
                gitRepoPath = Git.cloneOrUpdateRepository(configuration.gitPath(), configuration.cacheDirectory(), configuration.gitRepoUri(), configuration.gitBranch(), stdOut);

            } else {
                gitRepoPath = Git.assertExistingRepository(configuration.gitPath(), configuration.cacheDirectory(), configuration.gitRepoUri(), configuration.gitBranch());
            }
        } catch (final IOException e) {
            exit(EXIT_CODE_GIT_REPO_ERROR, e, stdErr);
            return;
        }

        // TODO(AR) auto-detect what sort of test suite it is
        final ParserConfiguration parserConfiguration = new ParserConfiguration(
                configuration.testSetPattern(),
                configuration.testSets(),
                configuration.excludeTestSets(),
                configuration.skipTestSets(),
                configuration.testCasePattern(),
                configuration.testCases(),
                configuration.excludeTestCases(),
                configuration.skipTestCases()
        );
        @Nullable final TestSuiteParser testSuiteParser;
        try {
            testSuiteParser = TestSuiteParserFactory.newParser(parserConfiguration, gitRepoPath);
        } catch (final IOException e) {
            exit(EXIT_CODE_PARSER_PROVIDER_IO_ERROR, e, stdErr);
            return;
        }
        if (testSuiteParser == null) {
            exit(EXIT_CODE_NO_SUITABLE_PARSER_AVAILABLE, null, stdErr);
            return;
        }

        stdOut.println("Detected Test Suite in: " + gitRepoPath);
        stdOut.println("Using parser: " + testSuiteParser.getParserName());

        if (configuration.verbose()) {
            testSuiteParser.addEventListener(new ParserEventListenerPrintStream(stdOut));
        }

        // Open Test Results listeners
        final List<TestResultsListener> testResultsListeners = new ArrayList<>();
        try {
            for (final Reporter reporter : reporters) {
                testResultsListeners.add(reporter.open());
            }

            // Execute the test suite
            final ThreadFactory testSuiteThreadFactory = Thread.ofVirtual().name("test-suite-", 0).factory();
            try (final StructuredTaskScope<Object, Void> testSuiteScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow(), config -> config.withThreadFactory(testSuiteThreadFactory))) {

                final ConnectorFactory<? extends TestCaseExecutionContext> connectorFactory = connectorFactories.get(0); // TODO(AR) allow for multiple connector factories

                final StructuredTaskScope.Subtask<Object> testSuiteSubTask = testSuiteScope.fork(new ExecuteTestSuiteTask(connectorFactory, testSuiteParser, testResultsListeners));

                try {
                    testSuiteScope.join();
                } catch (final StructuredTaskScope.FailedException e) {
                    if (e.getCause() instanceof ExecuteTestSuiteTask.ExecuteTestSuiteTaskException ee) {
                        exit(EXIT_CODE_PARSE_ERROR, ee, stdErr);
                        return;
                    } else if (e.getCause() instanceof ConnectorException ee) {
                        exit(EXIT_CODE_CONNECTOR_ERROR, ee, stdErr);
                        return;
                    }
                    throw e;
                }
            } catch (final InterruptedException e) {
                // restore thread's interrupted flag
                Thread.currentThread().interrupt();
                exit(EXIT_CODE_PROCESS_INTERRUPTED, e, stdErr);
            }
        } catch (final ReporterException e) {
            exit(EXIT_CODE_OPEN_REPORTER_ERROR, e, stdErr);
        } finally {
            for (final TestResultsListener testResultsListener : testResultsListeners) {
                testResultsListener.close();
            }
        }

        // TODO(AR) shutdown the Connectors when we are finished with them

        // TODO(AR) select one or more Processors (e.g. Elemental, eXist-db, Saxon, BaseX)

        // TODO(AR) execute the tasks one engine at a time - (choose parallelism per engine)

        // TODO(AR) write the results to Junit format

        // TODO(AR) produce a HTML report of the results,

        // TODO(AR) show a summary of the results on the console with a link to the Data and HTML report
    }



    private static void banner(final XthProperties xthProperties, final PrintStream out) {
        out.println("XTH Version: " + xthProperties.getProductVersion());
        out.println(xthProperties.getProductDescription());
        out.println(xthProperties.getProductCopyright());
        out.println();
    }

    private static void usage(final XthProperties xthProperties, final PrintStream out) {
        for (final ArgumentDefinitionGroup argumentDefinitionGroup : ArgumentDefinitionGroup.values()) {
            for (final ArgumentDefinition argumentDefinition : argumentDefinitionGroup.members) {
                argumentDefinition.printUsage(out, xthProperties);
            }
            out.println();
        }

        out.println();
    }

    private static void exit(final int exitCode, @Nullable final Throwable throwable, final PrintStream out) {
        if (throwable != null) {
            if (DEBUG) {
                throwable.printStackTrace(out);
            } else {
                out.println(throwable.getMessage());
            }
        }
        System.exit(exitCode);
    }

    private static Arguments parseArguments(final String[] args) throws IllegalArgumentException {
        final Arguments arguments = new Arguments();

        for (int i = 0; i < args.length; i++) {
            final String arg = args[i].trim();

            for (final ArgumentDefinition argumentDefinition : ArgumentDefinition.values()) {
                if (argumentDefinition.matchesArg(arg)) {
                    @Nullable final String parameter;
                    if (argumentDefinition.requiresParameter()) {
                        if (i + 1 == args.length) {
                            throw new IllegalArgumentException("Could not find parameter for argument: " + arg);
                        }
                        parameter = args[++i];

                        if (parameter.startsWith("-")) {
                            throw new IllegalArgumentException("Invalid parameter (" + parameter + ") for argument: " + arg);
                        }
                    } else {
                        parameter = null;
                    }

                    argumentDefinition.setArgumentFromValue(arguments, parameter);
                }
            }
        }

        return arguments;
    }

    private static Configuration createConfiguration(final XthProperties xthProperties, final Arguments arguments) {
        return new Configuration(
            arguments.getGitPath(),
            valueOrDefault(arguments.getQtGitRepoUri(), xthProperties.getDefaultQtGitRepoUri()),
            valueOrDefault(arguments.getQtGitBranch(), xthProperties.getDefaultQtGitBranch()),
            valueOrDefault(arguments.getCacheDirectory(), xthProperties.getDefaultCacheDirectory()),
            valueOrDefault(arguments.getOutputDirectory(), xthProperties.getDefaultOutputDirectory()),
            arguments.getTestSetPattern(),
            arguments.getTestSets(),
            arguments.getExcludeTestSets(),
            arguments.getSkipTestSets(),
            arguments.getTestCasePattern(),
            arguments.getTestCases(),
            arguments.getExcludeTestCases(),
            arguments.getSkipTestCases(),
            arguments.isVerbose()
        );
    }

    private static <T> T valueOrDefault(@Nullable final T value, final T defaultValue) {
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }
}
