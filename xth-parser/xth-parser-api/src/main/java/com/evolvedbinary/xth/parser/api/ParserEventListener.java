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

import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.TestSuite;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.UUID;

public interface ParserEventListener {
    void startParseCatalog(UUID parseId, Path catalogFile, EnumSet<SpecificationVersion> defaultSpecifications);

    void startParseCatalogEnvironments(UUID parseId);
    void catalogEnvironment(UUID parseId, EnvironmentDefinition environment);
    void endParseCatalogEnvironments(UUID parseId);

    void startParseTestSets(UUID parseId, TestSuite testSuite);
    void excludedTestSet(UUID parseId, UUID testSetId, Path testSetFile, String testSetName);
    void skippedTestSet(UUID parseId, UUID testSetId, Path testSetFile, String testSetName);
    void startParseTestSet(UUID parseId, UUID testSetId, TestSet testSet);
    void excludedTestCase(UUID parseId, UUID testSetId, UUID testCaseId, String testCaseName);
    void skippedTestCase(UUID parseId, UUID testSetId, UUID testCaseId, String testCaseName);
    void testCase(UUID parseId, UUID testSetId, UUID testCaseId, TestCase testCase);
    void endParseTestSet(UUID parseId, UUID testSetId);
    void endParseTestSets(UUID parseId, TestSuite testSuite);

    void endParseCatalog(UUID parseId);
}
