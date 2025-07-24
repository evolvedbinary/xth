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
package com.evolvedbinary.xth.parser.api;

import java.io.IOException;

/**
 * Interface for a Parser that can read a Test Suite
 * and generate parse events.
 */
public interface TestSuiteParser {

    /**
     * Get the name of the parser.
     *
     * Should be a short but humane name.
     *
     * @return the name of the parser.
     */
    default String getParserName() {
        return getClass().getSimpleName();
    }

    /**
     * Registers an event listener with the parser.
     *
     * The parser outputs events to the registered listeners.
     *
     * @param eventListener An event listener.
     */
    void addEventListener(ParserEventListener eventListener);

    /**
     * Deregister an event lister from the parser.
     *
     * @param eventListener An event listener.
     */
    void removeEventListener(ParserEventListener eventListener);

    /**
     * Parses the Test Suite.
     *
     * @throws IOException if an I/O error occurs whilst reading the test suite files.
     * @throws ParserException if an error occurs during parsing of the test suite files.
     */
    void parse() throws IOException, ParserException;
}
