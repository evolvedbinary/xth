package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.configuration.XthProperties;
import org.jspecify.annotations.Nullable;

import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum ArgumentDefinition {

    GIT_PATH(       "git-path",     "g", "path_to_git",         "\t\t\t",   "The path to the Git binary on your system. (default: expected to be found on the PATH)", null, setPathArgument(Arguments::setGitPath)),
    GIT_REPO_URI(   "git-repo-uri", "r", "uri_to_test_repo",    "\t",       "The URI of the Git Repository containing the Test Suite to run. (default: %s)", descriptionParams(XthProperties::getDefaultQtGitRepoUri), setUriArgument(Arguments::setQtGitRepoUri)),
    GIT_BRANCH(     "git-branch",   "b", "test_repo_branch",    "\t",       "The Git branch of the Git Repository to run. (default: %s)", descriptionParams(XthProperties::getDefaultQtGitBranch), setStringArgument(Arguments::setQtGitBranch)),

    CACHE_DIR(      "cache-dir",    "c", "path_to_cache_dir",   "\t",       "The directory to cache downloaded test suites in. (default: %s)", descriptionParams(XthProperties::getDefaultCacheDirectory), setPathArgument(Arguments::setCacheDirectory)),
    OUTPUT_DIR(     "output-dir",   "o", "path_to_output_dir",  "\t",       "The directory to write the output and results to. (default: %s)", descriptionParams(XthProperties::getDefaultOutputDirectory), setPathArgument(Arguments::setOutputDirectory)),
    VERBOSE(       "verbose",      "V", "verbose",             "\t\t\t",   "Produce verbose output on Stdout", null, setBooleanArgument(Arguments::setVerbose));

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
        return fullArg.equals(arg) || shortArg.equals("arg");
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
}
