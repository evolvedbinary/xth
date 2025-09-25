package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.SchemaAwareDependencyImpl;

public sealed interface SchemaAwareDependency extends Dependency<Boolean> permits SchemaAwareDependencyImpl {
}
