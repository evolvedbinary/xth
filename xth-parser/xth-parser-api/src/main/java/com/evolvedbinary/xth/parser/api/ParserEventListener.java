package com.evolvedbinary.xth.parser.api;

import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;

import java.nio.file.Path;
import java.util.UUID;

public interface ParserEventListener {
    void startParseCatalog(UUID parseId, Path catalogFile);

    void startParseCatalogEnvironments(UUID parseId);
    void catalogEnvironment(UUID parseId, EnvironmentDefinition environment);
    void endParseCatalogEnvironments(UUID parseId);

    void startParseTestSets(UUID parseId);
    void startParseTestSet(UUID parseId, UUID testSetId, TestSet testSet);
    void testCase(UUID parseId, UUID testSetId, UUID testCaseId, TestCase testCase);
    void endParseTestSet(UUID parseId, UUID testSetId);
    void endParseTestSets(UUID parseId);

    void endParseCatalog(UUID parseId);
}
