package com.evolvedbinary.xth.connector.elemental.embedded;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.spi.ConnectorProvider;

public class ElementalEmbeddedConnectorProvider implements ConnectorProvider {
    @Override
    public Connector newConnector() {
        return new ElementalEmbeddedConnector();
    }
}
