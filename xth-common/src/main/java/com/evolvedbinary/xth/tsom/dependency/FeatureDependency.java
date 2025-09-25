package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.Feature;
import com.evolvedbinary.xth.tsom.dependency.impl.FeatureDependencyImpl;

import java.util.Set;

public sealed interface FeatureDependency extends Dependency<Set<Feature>> permits FeatureDependencyImpl {
}
