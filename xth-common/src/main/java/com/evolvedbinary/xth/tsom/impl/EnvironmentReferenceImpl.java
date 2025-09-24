package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.EnvironmentReference;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public final class EnvironmentReferenceImpl extends AbstractEnvironment implements EnvironmentReference {

    private EnvironmentReferenceImpl(final URI baseUri, @Nullable final String name) {
        super(baseUri, name);
    }

    public static Builder builder(final URI baseUri, @Nullable final String name) {
        return new Builder(baseUri, name);
    }

    public static final class Builder extends AbstractEnvironment.Builder implements EnvironmentReference.Builder {
        private Builder(final URI baseUri, @Nullable final String name) {
            super(baseUri, name);
        }

        @Override
        public Environment build() {
            return new EnvironmentReferenceImpl(baseUri, name);
        }
    }
}
