package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.LimitsDependencyImpl;

public sealed interface LimitsDependency extends Dependency<String> permits LimitsDependencyImpl {
}
