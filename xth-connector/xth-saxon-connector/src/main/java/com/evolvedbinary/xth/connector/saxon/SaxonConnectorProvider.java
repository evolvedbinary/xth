package com.evolvedbinary.xth.connector.saxon;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.spi.ConnectorProvider;

public class SaxonConnectorProvider implements ConnectorProvider {
    @Override
    public Connector newConnector() {
        return new SaxonConnector();
    }
}
