package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.dependency.*;
import com.evolvedbinary.xth.tsom.dependency.impl.AbstractDependencyImpl;

public sealed interface Dependency<T> permits CalendarDependency, CollectionStabilityDependency, DefaultLanguageDependency, DirectoryAsCollectionUriDependency, FeatureDependency, FormatIntegerSequenceDependency, LanguageDependency, LimitsDependency, SchemaAwareDependency, SpecificationDependency, UnicodeNormalizationFormDependency, UnicodeVersionDependency, XmlVersionDependency, XsdVersionDependency, AbstractDependencyImpl {
    T getDependencyValue();
    boolean isSatisfied();
}
