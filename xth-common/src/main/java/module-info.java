module xth.common {
    exports com.evolvedbinary.xth.tsom;
    exports com.evolvedbinary.xth.tsom.impl;
    exports com.evolvedbinary.xth.tsom.assertion;
    exports com.evolvedbinary.xth.tsom.assertion.impl;
    exports com.evolvedbinary.xth.configuration;
    exports com.evolvedbinary.xth.scm;

    requires java.xml;
    requires static org.jspecify;
}