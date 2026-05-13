package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.configuration.Configuration;
import com.evolvedbinary.xth.configuration.XthProperties;
import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.connector.util.ConnectorFactory;
import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.reporting.otr.TestResultsListenerJUnitXml;
import com.evolvedbinary.xth.parser.api.ParserEventListener;
import com.evolvedbinary.xth.parser.api.ParserException;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.parser.util.TestSuiteParserFactory;
import com.evolvedbinary.xth.scm.Git;
import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
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
    private final static int EXIT_CODE_NO_CONNECTORS = 3;
    private final static int EXIT_CODE_CONNECTOR_ERROR = 4;
    private final static int EXIT_CODE_GIT_REPO_ERROR = 5;
    private final static int EXIT_CODE_PARSER_PROVIDER_IO_ERROR = 6;
    private final static int EXIT_CODE_NO_SUITABLE_PARSER_AVAILABLE = 7;
    private final static int EXIT_CODE_PARSE_ERROR = 8;
    private final static int EXIT_CODE_PROCESS_INTERRUPTED = 9;


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

        // TODO(AR) merge arguments and xthProperties to create a Configuration - report if the config is incomplete
        final Configuration configuration = createConfiguration(xthProperties, arguments);

        // TODO(AR) select connectors based on user args and capabilities
        final List<Connector> connectors = ConnectorFactory.connectors();
        if (connectors == null) {
            exit(EXIT_CODE_NO_CONNECTORS, new IllegalStateException("No connectors available"), stdErr);
        }
        for (final Connector connector : connectors) {
            stdOut.println(String.format("Loaded connector: %s", connector.getConnectorName()));
        }

        // TODO(AR) only print usage if we are missing args or have an invalid combo of args
        usage(xthProperties, stdOut);

        // TODO(AR) download a test suite via external git
        final Path gitRepoPath;
        try {
            gitRepoPath = Git.cloneOrUpdateRepository(configuration.gitPath(), configuration.cacheDirectory(), configuration.gitRepoUri(), configuration.gitBranch(), stdOut);
        } catch (final IOException e) {
            exit(EXIT_CODE_GIT_REPO_ERROR, e, stdErr);
            return;
        }

        // TODO(AR) auto-detect what sort of test suite it is
        @Nullable final TestSuiteParser testSuiteParser;
        try {
            testSuiteParser = TestSuiteParserFactory.newParser(gitRepoPath);
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

        // Execute the test suite
        final ThreadFactory testSuiteThreadFactory = Thread.ofVirtual().name("test-suite-", 0).factory();
        try (final StructuredTaskScope<Object, Void> testSuiteScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow(), config -> config.withThreadFactory(testSuiteThreadFactory))) {
            // TODO(AR) do stuff
            final Connector connector = connectors.get(0); // TODO(AR) allow for multiple connectors
            final List<TestResultsListener> testResultsListeners = List.of(
                    new TestResultsListenerPrintStream(stdOut),
                    new TestResultsListenerJUnitXml(configuration.outputDir())
            );
            final StructuredTaskScope.Subtask<Object> parse = testSuiteScope.fork(new ExecuteTestSuiteTask(connector, testSuiteParser, testResultsListeners));

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
                        parameter = args[i++];

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
