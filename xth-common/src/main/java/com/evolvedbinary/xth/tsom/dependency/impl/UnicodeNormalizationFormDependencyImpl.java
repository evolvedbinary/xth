package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.UnicodeNormalizationFormDependency;

public final class UnicodeNormalizationFormDependencyImpl extends AbstractDependencyImpl<String> implements UnicodeNormalizationFormDependency {
    public UnicodeNormalizationFormDependencyImpl(final String unicodeNormalizationForm, final boolean satisfied) {
        super(unicodeNormalizationForm, satisfied);
    }
}
