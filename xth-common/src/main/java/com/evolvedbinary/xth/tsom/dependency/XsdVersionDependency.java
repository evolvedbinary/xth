package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.XsdVersionDependencyImpl;

public sealed interface XsdVersionDependency extends Dependency<String> permits XsdVersionDependencyImpl {
}
