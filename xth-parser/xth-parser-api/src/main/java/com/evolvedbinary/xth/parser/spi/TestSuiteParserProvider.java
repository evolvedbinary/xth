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
package com.evolvedbinary.xth.parser.spi;

import com.evolvedbinary.xth.parser.api.ParserConfiguration;
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
    boolean canParse(Path testSuiteDirectory) throws IOException;

    /**
     * Create a new parser for the test suite.
     *
     * @param parserConfiguration configuration for the parser.
     * @param testSuiteDirectory a directory containing a test suite to parse.
     *
     * @return the parser, or null if this provider cannot create a parser that is suitable for parsing the test suite.
     *
     * @throws IOException if there is an issue reading from the test suite directory.
     */
    @Nullable TestSuiteParser newParser(ParserConfiguration parserConfiguration, final Path testSuiteDirectory) throws IOException;
}
