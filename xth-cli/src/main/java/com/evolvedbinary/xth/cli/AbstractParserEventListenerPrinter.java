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

import com.evolvedbinary.xth.parser.api.ParserEventListener;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractParserEventListenerPrinter implements ParserEventListener {

    private final Consumer<String> printer;

    protected AbstractParserEventListenerPrinter(final Consumer<String> printer) {
        this.printer = printer;
    }

    @Override
    public void startParseCatalog(final UUID parseId, final Path catalogFile, final EnumSet<SpecificationVersion> defaultSpecifications) {
        printer.accept(String.format("Starting to parse Catalog (%s): %s", parseId, catalogFile));
    }

    @Override
    public void startParseCatalogEnvironments(final UUID parseId) {
        printer.accept(String.format("Starting to parse Global Environments (%s)...", parseId));
    }

    @Override
    public void catalogEnvironment(final UUID parseId, final EnvironmentDefinition environment) {
        printer.accept(String.format("Global Environment (%s): %s", parseId, environment.getName()));
    }

    @Override
    public void endParseCatalogEnvironments(final UUID parseId) {
        printer.accept(String.format("Finished parsing Global Environments (%s).", parseId));
    }

    @Override
    public void startParseTestSets(final UUID parseId, final TestSuite testSuite) {
        printer.accept(String.format("Starting to parse TestSuite (%s): %s...", parseId, testSuite.getName()));
    }

    @Override
    public void excludedTestSet(final UUID parseId, final UUID testSetId, final Path testSetFile, final String testSetName) {
        printer.accept(String.format("Ignoring excluded Test Set (%s / %s): %s", parseId, testSetId, testSetName));
    }

    @Override
    public void skippedTestSet(final UUID parseId, final UUID testSetId, final Path testSetFile, final String testSetName) {
        printer.accept(String.format("Skipping Test Set (%s / %s): %s", parseId, testSetId, testSetName));
    }

    @Override
    public void startParseTestSet(final UUID parseId, final UUID testSetId, final TestSet testSet) {
        printer.accept(String.format("Starting to parse Test Set (%s / %s): %s", parseId, testSetId, testSet.getName()));
    }

    @Override
    public void excludedTestCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final String testCaseName) {
        printer.accept(String.format("Ignoring excluded Test Case (%s / %s / %s): %s", parseId, testSetId, testCaseId, testCaseName));
    }

    @Override
    public void skippedTestCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final String testCaseName) {
        printer.accept(String.format("Skipping Test Case (%s / %s / %s): %s", parseId, testSetId, testCaseId, testCaseName));
    }

    @Override
    public void testCase(final UUID parseId, final UUID testSetId, final UUID testCaseId, final TestCase testCase) {
        printer.accept(String.format("Test Case (%s / %s / %s): %s", parseId, testSetId, testCaseId, testCase.getName()));
    }

    @Override
    public void endParseTestSet(final UUID parseId, final UUID testSetId) {
        printer.accept(String.format("Finished parsing Test Set (%s / %s).", parseId, testSetId));
    }

    @Override
    public void endParseTestSets(final UUID parseId, final TestSuite testSuite) {
        printer.accept(String.format("Finished parsing TestSets (%s): %s.", parseId, testSuite.getName()));
    }

    @Override
    public void endParseCatalog(final UUID parseId) {
        printer.accept(String.format("Finished parsing Catalog (%s).", parseId));
    }
}
