package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.CollectionStabilityDependencyImpl;

public sealed interface CollectionStabilityDependency extends Dependency<Boolean> permits CollectionStabilityDependencyImpl {
}
