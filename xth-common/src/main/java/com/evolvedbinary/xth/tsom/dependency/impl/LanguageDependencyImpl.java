package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.LanguageDependency;

public final class LanguageDependencyImpl extends AbstractDependencyImpl<String> implements LanguageDependency {
    public LanguageDependencyImpl(final String languageCode, final boolean satisfied) {
        super(languageCode, satisfied);
    }
}
