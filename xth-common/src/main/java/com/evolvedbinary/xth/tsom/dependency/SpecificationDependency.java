package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.SpecificationDependencyImpl;

import java.util.Set;

public sealed interface SpecificationDependency extends Dependency<Set<SpecificationVersionDescription>> permits SpecificationDependencyImpl {
}
