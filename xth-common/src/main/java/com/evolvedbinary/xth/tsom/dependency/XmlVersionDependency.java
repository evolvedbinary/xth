package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.XmlVersionDependencyImpl;

public sealed interface XmlVersionDependency extends Dependency<String> permits XmlVersionDependencyImpl {
}
