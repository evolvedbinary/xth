package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.AbstractEnvironment;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed interface Environment permits EnvironmentReference, EnvironmentDefinition, AbstractEnvironment {

    /**
     * Get the BaseURI of the environment.
     */
    URI getBaseUri();

    /**
     * Get the name of the Environment.
     *
     * @return the name of the Environment.
     */
    @Nullable String getName();

    sealed interface Builder permits EnvironmentReference.Builder, EnvironmentDefinition.Builder, AbstractEnvironment.Builder {
        Environment build();
    }
}
