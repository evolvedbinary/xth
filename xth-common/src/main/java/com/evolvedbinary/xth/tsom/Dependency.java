package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.DependencyImpl;

public sealed interface Dependency permits DependencyImpl {
    DependencyType getType();
    String getValue();
    boolean isSatisfied();
}
