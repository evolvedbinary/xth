package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.configuration.Configuration;
import com.evolvedbinary.xth.configuration.XthProperties;
import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.util.ConnectorFactory;
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
import java.util.EventListener;
import java.util.List;
import java.util.UUID;

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
    private final static int EXIT_CODE_GIT_REPO_ERROR = 4;
    private final static int EXIT_CODE_PARSER_PROVIDER_IO_ERROR = 5;
    private final static int EXIT_CODE_NO_SUITABLE_PARSER_AVAILABLE = 6;
    private final static int EXIT_CODE_PARSE_ERROR = 7;


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

        // TODO(AR) allow multiple test sets to be run?
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

        testSuiteParser.addEventListener(new ParserEventListener() {

            @Override
            public void startParseCatalog(final UUID parseId, final Path catalogFile) {
                stdOut.println(String.format("Starting to parse Catalog (%s): %s", parseId, catalogFile));
            }

            @Override
            public void startParseCatalogEnvironments(final UUID parseId) {
                stdOut.println(String.format("* Starting to parse Global Environments (%s)...", parseId));
            }

            @Override
            public void catalogEnvironment(final UUID parseId, final Environment environment) {
                stdOut.println(String.format("\tGlobal Environment (%s): %s", parseId, environment.getName()));
            }

            @Override
            public void endParseCatalogEnvironments(final UUID parseId) {
                stdOut.println(String.format("* Finished parsing Global Environments (%s).", parseId));
            }

            @Override
            public void startParseTestSets(final UUID parseId) {
                stdOut.println(String.format("* Starting to parse TestSets (%s)...", parseId));
            }

            @Override
            public void startParseTestSet(final UUID parseId, final UUID testSetId, final TestSet testSet) {
                stdOut.println(String.format("\tStarting to parse Test Set (%s / %s): %s", parseId, testSetId, testSet.getName()));
            }

            @Override
            public void testCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final TestCase testCase) {
                stdOut.println(String.format("\t\tTest Case (%s / %s / %s): %s", parseId, testSetId, testCaseId, testCase.getName()));
            }

            @Override
            public void endParseTestSet(final UUID parseId, final UUID testSetId) {
                stdOut.println(String.format("\tFinished parsing Test Set (%s / %s).", parseId, testSetId));
            }

            @Override
            public void endParseTestSets(final UUID parseId) {
                stdOut.println(String.format("* Finished parsing TestSets (%s).", parseId));
            }

            @Override
            public void endParseCatalog(final UUID parseId) {
                stdOut.println(String.format("Finished parsing Catalog (%s).", parseId));
            }
        });



        // TODO(AR) parse the test suite with the appropriate parser - maybe this should generate a number of tasks in a queue?
        try {
            testSuiteParser.parse();
        } catch (final IOException | ParserException e) {
            exit(EXIT_CODE_PARSE_ERROR, e, stdErr);
            return;
        }

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
            valueOrDefault(arguments.getOutputDirectory(), xthProperties.getDefaultOutputDirectory())
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
