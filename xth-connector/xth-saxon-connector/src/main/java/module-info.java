module xth.connector.impl.saxon {

    provides com.evolvedbinary.xth.connector.spi.ConnectorProvider with com.evolvedbinary.xth.connector.saxon.SaxonConnectorProvider;

    requires xth.connector.api;
    requires xth.common;
    requires java.xml;
    requires Saxon.HE;
    requires static org.jspecify;
    requires static jcip.annotations;
}