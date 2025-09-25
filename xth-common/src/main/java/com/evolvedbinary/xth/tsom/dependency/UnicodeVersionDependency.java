package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.UnicodeVersionDependencyImpl;

public sealed interface UnicodeVersionDependency extends Dependency<String> permits UnicodeVersionDependencyImpl {
}
