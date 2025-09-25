package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.LimitsDependency;

public final class LimitsDependencyImpl extends AbstractDependencyImpl<String> implements LimitsDependency {
    public LimitsDependencyImpl(final String limits, final boolean satisfied) {
        super(limits, satisfied);
    }
}
