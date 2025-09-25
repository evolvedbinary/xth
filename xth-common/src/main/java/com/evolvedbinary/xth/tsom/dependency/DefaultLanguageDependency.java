package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.DefaultLanguageDependencyImpl;

public sealed interface DefaultLanguageDependency extends Dependency<String> permits DefaultLanguageDependencyImpl {
}
