package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;

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
    public void startParseTestSets(final UUID parseId) {
        out.print("* ");
        super.startParseTestSets(parseId);
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
    public void endParseTestSets(final UUID parseId) {
        out.print("* ");
        super.endParseTestSets(parseId);
    }
}
