package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.LanguageDependencyImpl;

public sealed interface LanguageDependency extends Dependency<String> permits LanguageDependencyImpl {
}
