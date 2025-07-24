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

import com.evolvedbinary.xth.configuration.XthProperties;
import org.jspecify.annotations.Nullable;

import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public enum ArgumentDefinition {

    GIT_PATH(           "git-path",             "g",        "path_to_git",          "\t\t\t\t\t",   "The path to the Git binary on your system. (default: expected to be found on the PATH)", null, setPathArgument(Arguments::setGitPath)),
    GIT_REPO_URI(       "git-repo-uri",         "r",        "uri_to_test_repo",     "\t\t\t",       "The URI of the Git Repository containing the Test Suite to run. (default: %s)", descriptionParams(XthProperties::getDefaultQtGitRepoUri), setUriArgument(Arguments::setQtGitRepoUri)),
    GIT_BRANCH(         "git-branch",           "b",        "test_repo_branch",     "\t\t\t",       "The Git branch of the Git Repository to run. (default: %s)", descriptionParams(XthProperties::getDefaultQtGitBranch), setStringArgument(Arguments::setQtGitBranch)),

    CACHE_DIR(          "cache-dir",            "c",        "path_to_cache_dir",    "\t\t\t",       "The directory to cache downloaded test suites in. (default: %s)", descriptionParams(XthProperties::getDefaultCacheDirectory), setPathArgument(Arguments::setCacheDirectory)),
    OUTPUT_DIR(         "output-dir",           "o",        "path_to_output_dir",   "\t\t\t",       "The directory to write the output and results to. (default: %s)", descriptionParams(XthProperties::getDefaultOutputDirectory), setPathArgument(Arguments::setOutputDirectory)),

    TEST_SET_PATTERN(   "test-set-pattern",     "tsp",      "regular_expression",   "\t",           "A regular expression that restricts the test-sets to be executed; matched against the name of th test-set.", null, setPatternArgument(Arguments::setTestSetPattern)),
    TEST_SET(           "test-set",             "ts",       "test_set_name(s)",     "\t\t\t\t",     "A comma separated list of names of test-sets to be executed.", null, setStringArrayArgument(Arguments::setTestSets)),
    EXCLUDE_TEST_SET(   "exclude-test-set",     "tsx",      "test_set_name(s)",     "\t",           "A comma separated list of names of test-sets to be excluded from execution. During normal use, the argument skip-test-set should be preferred.", null, setStringArrayArgument(Arguments::setExcludeTestSets)),
    SKIP_TEST_SET(      "skip-test-set",        "tss",      "test_set_name(s)",     "\t",           "A comma separated list of names of test-sets to be marked as skipped during execution.", null, setStringArrayArgument(Arguments::setSkipTestSets)),

    TEST_CASE_PATTERN(   "test-case-pattern",   "tcp",      "regular_expression",   "\t",           "A regular expression that restricts the test-cases to be executed; matched against the name of th test-set.", null, setPatternArgument(Arguments::setTestCasePattern)),
    TEST_CASE(           "test-case",           "tc",       "test_case_name(s)",    "\t\t\t",       "A comma separated list of names of test-cases to be executed.", null, setStringArrayArgument(Arguments::setTestCases)),
    EXCLUDE_TEST_CASE(   "exclude-test-case",   "tcx",      "test_case_name(s)",    "\t",           "A comma separated list of names of test-cases to be excluded from execution. During normal use, the argument skip-test-set should be preferred.", null, setStringArrayArgument(Arguments::setExcludeTestCases)),
    SKIP_TEST_CASE(      "skip-test-case",      "tcs",      "test_case_name(s)",    "\t",           "A comma separated list of names of test-cases to be marked as skipped during execution.", null, setStringArrayArgument(Arguments::setSkipTestCases)),

    OFFLINE_MODE(        "offline-mode",         "O",        null,                  "\t\t",        "Do not attempt to connect to the Web", null, setBooleanArgumentToTrue(Arguments::setOfflineMode)),
    VERBOSE(             "verbose",              "V",        null,                  "\t\t\t",      "Produce verbose output on Stdout", null, setBooleanArgumentToTrue(Arguments::setVerbose)),
    SHOW_HELP(           "help",                 "h",        null,                  "\t\t\t\t\t",  "Show help information", null, setBooleanArgumentToTrue(Arguments::setShowHelp));


    public final String fullArg;
    public final String shortArg;
    final @Nullable String parameter;
    final String separator;
    final String description;
    final @Nullable Function<XthProperties, Object>[] descriptionParameters;
    final BiFunction<Arguments, String, Arguments> setter;

    ArgumentDefinition(final String fullArg, final String shortArg, @Nullable final String parameter, final String separator, final String description, @Nullable final Function<XthProperties, Object>[] descriptionParameters, final BiFunction<Arguments, String, Arguments> setter) {
        this.fullArg = fullArg;
        this.shortArg = shortArg;
        this.parameter = parameter;
        this.separator = separator;
        this.description = description;
        this.descriptionParameters = descriptionParameters;
        this.setter = setter;
    }

    public void printUsage(final PrintStream out, final XthProperties xthProperties) {
        final Object[] descriptionArgs;
        if (descriptionParameters != null) {
            descriptionArgs = new Object[descriptionParameters.length];
            for (int i = 0; i < descriptionParameters.length; i++) {
                descriptionArgs[i] = descriptionParameters[i].apply(xthProperties);
            }
        } else {
            descriptionArgs = null;
        }

        out.println(fullArg() + "|" + shortArg() + (parameter != null ? " " + parameter : "") + separator + String.format(description, descriptionArgs));
    }

    public String fullArg() {
        return "--" + fullArg;
    }

    public String shortArg() {
        return "-" + shortArg;
    }

    public boolean matchesArg(final String arg) {
        return arg.matches(fullArg()) || arg.matches(shortArg());
    }

    public boolean requiresParameter() {
        return parameter != null;
    }

    public Arguments setArgumentFromValue(final Arguments arguments, @Nullable final String parameter) {
        setter.apply(arguments, parameter);
        return arguments;
    }

    private static Function<XthProperties, Object>[] descriptionParams(final Function<XthProperties, Object>... descriptionParams) {
        return descriptionParams;
    }

    private static BiFunction<Arguments, String, Arguments> setStringArgument(final BiConsumer<Arguments, String> setter) {
        return (arguments, parameter) -> {
            setter.accept(arguments, parameter);
            return arguments;
        };
    }

    private static BiFunction<Arguments, String, Arguments> setPatternArgument(final BiConsumer<Arguments, Pattern> setter) {
        return (arguments, parameter) -> {
            final Pattern pattern = Pattern.compile(parameter);
            setter.accept(arguments, pattern);
            return arguments;
        };
    }

    private static BiFunction<Arguments, String, Arguments> setPathArgument(final BiConsumer<Arguments, Path> setter) {
        return (arguments, parameter) -> {
            final Path path = Paths.get(parameter).normalize();
            setter.accept(arguments, path);
            return arguments;
        };
    }

    private static BiFunction<Arguments, String, Arguments> setUriArgument(final BiConsumer<Arguments, URI> setter) {
        return (arguments, parameter) -> {
            final URI uri = URI.create(parameter);
            setter.accept(arguments, uri);
            return arguments;
        };
    }

    private static BiFunction<Arguments, String, Arguments> setBooleanArgument(final BiConsumer<Arguments, Boolean> setter) {
        return (arguments, parameter) -> {
            final Boolean bool = Boolean.parseBoolean(parameter);
            setter.accept(arguments, bool);
            return arguments;
        };
    }

    private static BiFunction<Arguments, @Nullable String, Arguments> setBooleanArgumentToTrue(final BiConsumer<Arguments, Boolean> setter) {
        return (arguments, parameter) -> {
            setter.accept(arguments, true);
            return arguments;
        };
    }

    private static BiFunction<Arguments, String, Arguments> setStringArrayArgument(final BiConsumer<Arguments, @Nullable String[]> setter) {
        return (arguments, parameter) -> {
            final String[] array = parameter.split(",");

            @Nullable String[] cleanArray;
            if (array.length == 0) {
                cleanArray = null;
            } else {
                cleanArray = new String[array.length];
                int i = 0;
                for (String string : array) {
                    string = string.trim();
                    if (!string.isEmpty()) {
                        cleanArray[i++] = string;
                    }
                }
                cleanArray = Arrays.copyOf(cleanArray, i);
            }

            setter.accept(arguments, cleanArray);

            return arguments;
        };
    }
}
