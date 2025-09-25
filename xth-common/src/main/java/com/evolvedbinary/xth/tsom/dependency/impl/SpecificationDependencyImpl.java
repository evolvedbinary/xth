package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.SpecificationDependency;
import com.evolvedbinary.xth.tsom.dependency.SpecificationVersionDescription;

import java.util.Set;

public final class SpecificationDependencyImpl extends AbstractDependencyImpl<Set<SpecificationVersionDescription>> implements SpecificationDependency {
    public SpecificationDependencyImpl(final Set<SpecificationVersionDescription> specificationVersions, final boolean satisfied) {
        super(specificationVersions, satisfied);
    }
}
