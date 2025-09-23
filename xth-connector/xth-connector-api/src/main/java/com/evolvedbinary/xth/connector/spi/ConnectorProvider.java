package com.evolvedbinary.xth.connector.spi;

import com.evolvedbinary.xth.connector.api.Connector;

public interface ConnectorProvider {
    Connector newConnector();
}
