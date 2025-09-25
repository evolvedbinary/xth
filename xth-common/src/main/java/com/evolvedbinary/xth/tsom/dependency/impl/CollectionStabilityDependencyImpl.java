package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.CollectionStabilityDependency;

public final class CollectionStabilityDependencyImpl extends AbstractDependencyImpl<Boolean> implements CollectionStabilityDependency {
    public CollectionStabilityDependencyImpl(final boolean collectionStability, final boolean satisfied) {
        super(collectionStability, satisfied);
    }
}
