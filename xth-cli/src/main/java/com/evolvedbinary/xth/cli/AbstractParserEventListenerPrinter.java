package com.evolvedbinary.xth.cli;

import com.evolvedbinary.xth.parser.api.ParserEventListener;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractParserEventListenerPrinter implements ParserEventListener {

    private final Consumer<String> printer;

    protected AbstractParserEventListenerPrinter(final Consumer<String> printer) {
        this.printer = printer;
    }

    @Override
    public void startParseCatalog(final UUID parseId, final Path catalogFile) {
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
    public void startParseTestSets(final UUID parseId) {
        printer.accept(String.format("Starting to parse TestSets (%s)...", parseId));
    }

    @Override
    public void startParseTestSet(final UUID parseId, final UUID testSetId, final TestSet testSet) {
        printer.accept(String.format("Starting to parse Test Set (%s / %s): %s", parseId, testSetId, testSet.getName()));
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
    public void endParseTestSets(final UUID parseId) {
        printer.accept(String.format("Finished parsing TestSets (%s).", parseId));
    }

    @Override
    public void endParseCatalog(final UUID parseId) {
        printer.accept(String.format("Finished parsing Catalog (%s).", parseId));
    }
}
