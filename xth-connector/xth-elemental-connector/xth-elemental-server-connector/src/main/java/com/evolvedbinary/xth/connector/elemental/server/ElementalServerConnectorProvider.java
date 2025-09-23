package com.evolvedbinary.xth.connector.elemental.server;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.spi.ConnectorProvider;

public class ElementalServerConnectorProvider implements ConnectorProvider {
    @Override
    public Connector newConnector() {
        return new ElementalServerConnector();
    }
}
