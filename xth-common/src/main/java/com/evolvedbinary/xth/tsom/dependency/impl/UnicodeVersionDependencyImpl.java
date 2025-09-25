package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.UnicodeVersionDependency;

public final class UnicodeVersionDependencyImpl extends AbstractDependencyImpl<String> implements UnicodeVersionDependency {
    public UnicodeVersionDependencyImpl(final String unicodeVersion, final boolean satisfied) {
        super(unicodeVersion, satisfied);
    }
}
