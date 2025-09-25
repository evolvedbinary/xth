package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.DefaultLanguageDependency;

public final class DefaultLanguageDependencyImpl extends AbstractDependencyImpl<String> implements DefaultLanguageDependency {
    public DefaultLanguageDependencyImpl(final String defaultLanguage, final boolean satisfied) {
        super(defaultLanguage, satisfied);
    }
}
