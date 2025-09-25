package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.Dependency;

public sealed class AbstractDependencyImpl<T> implements Dependency<T> permits CalendarDependencyImpl, CollectionStabilityDependencyImpl, DefaultLanguageDependencyImpl, DirectoryAsCollectionUriDependencyImpl, FeatureDependencyImpl, FormatIntegerSequenceDependencyImpl, LanguageDependencyImpl, LimitsDependencyImpl, SchemaAwareDependencyImpl, SpecificationDependencyImpl, UnicodeNormalizationFormDependencyImpl, UnicodeVersionDependencyImpl, XmlVersionDependencyImpl, XsdVersionDependencyImpl {
    private final T dependencyValue;
    private final boolean satisfied;

    public AbstractDependencyImpl(final T dependencyValue, final boolean satisfied) {
        this.dependencyValue = dependencyValue;
        this.satisfied = satisfied;
    }

    @Override
    public boolean isSatisfied() {
        return satisfied;
    }

    @Override
    public T getDependencyValue() {
        return dependencyValue;
    }
}
