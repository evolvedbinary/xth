package com.evolvedbinary.xth.connector.api;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;

import java.nio.file.Path;
import java.util.List;

public interface Connector {
    String getConnectorName();
    String getImplementationName() throws ConnectorException;
    String getImplementationVersion() throws ConnectorException;
    void initialize(Path baseUri, SpecificationVersion defaultSpecification, List<EnvironmentDefinition> globalEnvironments) throws ConnectorException;

    /**
     * Checks whether the implementation supports the required dependencies.
     *
     * @param dependencies the required dependencies to check for.
     *
     * @return An empty list if all dependencies are supported, otherwise a list of the unsupported dependencies
     */
    List<Dependency<?>> supports(final List<Dependency<?>> dependencies);

    TestCaseResult executeTestCase(TestSet testSet, TestCase testCase) throws ConnectorException;
}
