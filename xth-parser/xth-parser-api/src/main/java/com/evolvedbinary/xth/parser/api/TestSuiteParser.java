package com.evolvedbinary.xth.parser.api;

import java.io.IOException;

public interface TestSuiteParser {
    String getParserName();
    void addEventListener(ParserEventListener eventListener);
    void removeEventListener(ParserEventListener eventListener);
    void parse() throws IOException, ParserException;
}
