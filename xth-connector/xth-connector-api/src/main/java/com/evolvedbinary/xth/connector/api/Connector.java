package com.evolvedbinary.xth.connector.api;

import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;

import java.util.List;

public interface Connector {
    String getConnectorName();
    String getImplementationName() throws ConnectorException;
    String getImplementationVersion() throws ConnectorException;
    void initialize(List<EnvironmentDefinition> globalEnvironments) throws ConnectorException;
    void executeTestCase(TestSet testSet, TestCase testCase) throws ConnectorException;
}
