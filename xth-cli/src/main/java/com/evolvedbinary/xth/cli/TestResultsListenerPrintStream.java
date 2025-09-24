package com.evolvedbinary.xth.cli;

import java.io.PrintStream;

public class TestResultsListenerPrintStream extends AbstractTestResultsListenerPrinter {

    public TestResultsListenerPrintStream(final PrintStream out) {
        super(out::println);
    }
}
