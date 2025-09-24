package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.*;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed abstract class AbstractEnvironment implements Environment permits EnvironmentReferenceImpl, EnvironmentDefinitionImpl {

    private final URI baseUri;
    @Nullable private final String name;

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    @Override
    public @Nullable String getName() {
        return name;
    }

    protected AbstractEnvironment(final URI baseUri, @Nullable final String name) {
        this.baseUri = baseUri;
        this.name = name;
    }

    public abstract static sealed class Builder implements Environment.Builder permits EnvironmentReferenceImpl.Builder, EnvironmentDefinitionImpl.Builder {
        protected final URI baseUri;
        protected final String name;

        protected Builder(final URI baseUri, final String name) {
            this.baseUri = baseUri;
            this.name = name;
        }
    }
}
