module xth.common {
    exports com.evolvedbinary.xth.tsom;
    exports com.evolvedbinary.xth.tsom.impl to xth.parser.impl.fots31;
    exports com.evolvedbinary.xth.tsom.assertion;
    exports com.evolvedbinary.xth.tsom.assertion.impl to xth.parser.impl.fots31;
    exports com.evolvedbinary.xth.tsom.dependency;
    exports com.evolvedbinary.xth.tsom.dependency.impl to xth.parser.impl.fots31, xth.connector.impl.saxon;
    exports com.evolvedbinary.xth.tsom.result;
    exports com.evolvedbinary.xth.tsom.result.impl to xth.cli, xth.connector.impl.saxon;
    exports com.evolvedbinary.xth.configuration;
    exports com.evolvedbinary.xth.scm;
    exports com.evolvedbinary.xth.util;

    requires java.xml;
    requires static org.jspecify;
}