package com.evolvedbinary.xth.parser.util;

import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.parser.spi.TestSuiteParserProvider;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.ServiceLoader;

public class TestSuiteParserFactory {

    /**
     * Create a new parser for the test suite.
     *
     * @param testSuiteDirectory a directory containing a test suite to parse.
     *
     * @return the parser, or null if a parser that is suitable for parsing the test suite cannot be found.
     *
     * @throws IOException if there is an issue reading from the test suite directory.
     */
    public static @Nullable TestSuiteParser newParser(final Path testSuiteDirectory) throws IOException {
        final ServiceLoader<TestSuiteParserProvider> serviceLoader = ServiceLoader.load(TestSuiteParserProvider.class);

        final Iterator<TestSuiteParserProvider> itServiceLoader = serviceLoader.iterator();
        while (itServiceLoader.hasNext()) {
            final TestSuiteParserProvider testSuiteParserProvider = itServiceLoader.next();

            final TestSuiteParser testSuiteParser = testSuiteParserProvider.newParser(testSuiteDirectory);
            if (testSuiteParser != null) {
                return testSuiteParser;
            }
        }

        return null;
    }
}
