module xth.parser.impl.fots31 {

    provides com.evolvedbinary.xth.parser.spi.TestSuiteParserProvider with com.evolvedbinary.xth.parser.fots31.FOTS31ParserProvider;

    opens org.w3._2010._09.qt_fots_catalog to jakarta.xml.bind;

    requires xth.parser.api;
    requires xth.common;
    requires com.fasterxml.aalto;
    requires jakarta.xml.bind;
    requires jakarta.validation;
    requires static org.jspecify;
}