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
package com.evolvedbinary.xth.parser.util;

import com.evolvedbinary.xth.parser.api.ParserConfiguration;
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
    public static @Nullable TestSuiteParser newParser(final ParserConfiguration parserConfiguration, final Path testSuiteDirectory) throws IOException {
        final ServiceLoader<TestSuiteParserProvider> serviceLoader = ServiceLoader.load(TestSuiteParserProvider.class);

        final Iterator<TestSuiteParserProvider> itServiceLoader = serviceLoader.iterator();
        while (itServiceLoader.hasNext()) {
            final TestSuiteParserProvider testSuiteParserProvider = itServiceLoader.next();

            final TestSuiteParser testSuiteParser = testSuiteParserProvider.newParser(parserConfiguration, testSuiteDirectory);
            if (testSuiteParser != null) {
                return testSuiteParser;
            }
        }

        return null;
    }
}
