package com.evolvedbinary.xth.parser.api;

import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.TestSet;

public interface ParserEventListener {
    void startParseCatalog();
    void catalogEnvironment(Environment environment);
    void testSet(TestSet testSet);
    void endParseCatalog();
}
