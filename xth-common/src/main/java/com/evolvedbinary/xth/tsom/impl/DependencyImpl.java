package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.DependencyType;

public final class DependencyImpl implements Dependency {
    private final DependencyType type;
    private final String value;
    private final boolean satisfied;

    public DependencyImpl(final DependencyType type, final String value, final boolean satisfied) {
        this.type = type;
        this.value = value;
        this.satisfied = satisfied;
    }

    @Override
    public DependencyType getType() {
        return type;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isSatisfied() {
        return satisfied;
    }
}
