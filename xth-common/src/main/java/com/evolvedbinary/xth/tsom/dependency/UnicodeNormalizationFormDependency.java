package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.UnicodeNormalizationFormDependencyImpl;

public sealed interface UnicodeNormalizationFormDependency extends Dependency<String> permits UnicodeNormalizationFormDependencyImpl {
}
