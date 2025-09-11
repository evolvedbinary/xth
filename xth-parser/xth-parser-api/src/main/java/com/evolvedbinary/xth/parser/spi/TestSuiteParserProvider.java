package com.evolvedbinary.xth.parser.spi;

import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;

public interface TestSuiteParserProvider {

    /**
     * Determine if this provider can create a parser
     * to parse the provided test suite.
     *
     * @param testSuiteDirectory a directory containing a test suite to parse.
     *
     * @return true if this provider can create a parser that can parse the provided test suite, false otherwise.
     *
     * @throws IOException if there is an issue reading from the test suite directory.
     */
    boolean canParse(final Path testSuiteDirectory) throws IOException;

    /**
     * Create a new parser for the test suite.
     *
     * @param testSuiteDirectory a directory containing a test suite to parse.
     *
     * @return the parser, or null if this provider cannot create a parser that is suitable for parsing the test suite.
     *
     * @throws IOException if there is an issue reading from the test suite directory.
     */
    @Nullable TestSuiteParser newParser(final Path testSuiteDirectory) throws IOException;
}
