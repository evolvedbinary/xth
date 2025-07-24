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

import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;

import java.io.PrintStream;
import java.util.UUID;

public class ParserEventListenerPrintStream extends AbstractParserEventListenerPrinter {

    private final PrintStream out;

    public ParserEventListenerPrintStream(final PrintStream out) {
        super(out::println);
        this.out = out;
    }

    @Override
    public void startParseCatalogEnvironments(final UUID parseId) {
        out.print("* ");
        super.startParseCatalogEnvironments(parseId);
    }

    @Override
    public void catalogEnvironment(final UUID parseId, final EnvironmentDefinition environment) {
        out.print('\t');
        super.catalogEnvironment(parseId, environment);
    }

    @Override
    public void endParseCatalogEnvironments(final UUID parseId) {
        out.print("* ");
        super.endParseCatalogEnvironments(parseId);
    }

    @Override
    public void startParseTestSets(final UUID parseId, final TestSuite testSuite) {
        out.print("* ");
        super.startParseTestSets(parseId, testSuite);
    }

    @Override
    public void startParseTestSet(final UUID parseId, final UUID testSetId, final TestSet testSet) {
        out.print('\t');
        super.startParseTestSet(parseId, testSetId, testSet);
    }

    @Override
    public void testCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final TestCase testCase) {
        out.print("\t\t");
        super.testCase(parseId, testSetId, testCaseId, testCase);
    }

    @Override
    public void endParseTestSet(final UUID parseId, final UUID testSetId) {
        out.print('\t');
        super.endParseTestSet(parseId, testSetId);
    }

    @Override
    public void endParseTestSets(final UUID parseId, final TestSuite testSuite) {
        out.print("* ");
        super.endParseTestSets(parseId, testSuite);
    }
}
