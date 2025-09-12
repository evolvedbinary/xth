package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.*;
import org.jspecify.annotations.Nullable;

public abstract class AbstractEnvironment implements Environment {

    @Nullable private final String name;

    @Override
    public @Nullable String getName() {
        return name;
    }

    protected AbstractEnvironment(final String name) {
        this.name = name;
    }


    protected abstract static class Builder implements Environment.Builder {
        protected final String name;

        protected Builder(final String name) {
            this.name = name;
        }
    }
}
