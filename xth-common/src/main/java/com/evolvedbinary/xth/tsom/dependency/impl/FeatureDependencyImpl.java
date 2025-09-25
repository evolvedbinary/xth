package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.Feature;
import com.evolvedbinary.xth.tsom.dependency.FeatureDependency;

import java.util.Set;


public final class FeatureDependencyImpl extends AbstractDependencyImpl<Set<Feature>> implements FeatureDependency {
    public FeatureDependencyImpl(final Set<Feature> features, final boolean satisfied) {
        super(features, satisfied);
    }
}
