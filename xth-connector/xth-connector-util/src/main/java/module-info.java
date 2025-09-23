module xth.connector.util {
    exports com.evolvedbinary.xth.connector.util;

    uses com.evolvedbinary.xth.connector.spi.ConnectorProvider;

    requires xth.connector.api;
    requires static org.jspecify;
}