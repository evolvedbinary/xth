package com.evolvedbinary.xth.connector.util;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.spi.ConnectorProvider;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ConnectorFactory {

    public static @Nullable List<Connector> connectors() {
        final ServiceLoader<ConnectorProvider> serviceLoader = ServiceLoader.load(ConnectorProvider.class);
        final Iterator<ConnectorProvider> itServiceLoader = serviceLoader.iterator();

        List<Connector> connectors = null;

        while (itServiceLoader.hasNext()) {
            final ConnectorProvider connectorProvider = itServiceLoader.next();
            final Connector connector = connectorProvider.newConnector();
            if (connector != null) {
                if (connectors == null) {
                    connectors = new ArrayList<>();
                }
                connectors.add(connector);
            }
        }

        if (connectors != null) {
            // make returned list immutable
            connectors = Collections.unmodifiableList(connectors);
        }

        return connectors;
    }
}
