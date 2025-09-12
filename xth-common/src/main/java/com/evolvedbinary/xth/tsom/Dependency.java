package com.evolvedbinary.xth.tsom;

public interface Dependency {
    DependencyType getType();
    String getValue();
    boolean isSatisfied();
}
