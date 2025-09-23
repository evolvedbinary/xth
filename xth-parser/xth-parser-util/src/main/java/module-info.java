module xth.parser.util {
    exports com.evolvedbinary.xth.parser.util;

    uses com.evolvedbinary.xth.parser.spi.TestSuiteParserProvider;

    requires xth.parser.api;
    requires static org.jspecify;
}