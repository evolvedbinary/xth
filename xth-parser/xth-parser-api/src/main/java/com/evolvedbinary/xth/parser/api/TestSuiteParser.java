package com.evolvedbinary.xth.parser.api;

import java.io.IOException;

public interface TestSuiteParser {

    String getParserName();

    void parse() throws IOException, ParserException;
}
