package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.EnvironmentReference;

public class EnvironmentReferenceImpl extends AbstractEnvironment implements EnvironmentReference {

    public EnvironmentReferenceImpl(final String name) {
        super(name);
    }

    public static Builder builder(final String name) {
        return new Builder(name);
    }

    public static class Builder extends AbstractEnvironment.Builder implements EnvironmentReference.Builder {
        private Builder(final String name) {
            super(name);
        }

        @Override
        public Environment build() {
            return new EnvironmentReferenceImpl(name);
        }
    }
}
