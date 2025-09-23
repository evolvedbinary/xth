package com.evolvedbinary.xth.connector.elemental.server;

import com.evolvedbinary.xth.connector.api.Connector;
import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;

import java.util.List;

public class ElementalServerConnector implements Connector {
    @Override
    public String getConnectorName() {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }

    @Override
    public String getImplementationName() throws ConnectorException {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }

    @Override
    public String getImplementationVersion() throws ConnectorException {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }

    @Override
    public void initialize(final List<EnvironmentDefinition> globalEnvironments) throws ConnectorException {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }

    @Override
    public void executeTestCase(final TestSet testSet, final TestCase testCase) throws ConnectorException {
        throw new UnsupportedOperationException("TODO(AR) implement");
    }
}
