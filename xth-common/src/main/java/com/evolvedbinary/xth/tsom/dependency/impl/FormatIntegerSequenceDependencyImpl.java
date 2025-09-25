package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.FormatIntegerSequenceDependency;

public final class FormatIntegerSequenceDependencyImpl extends AbstractDependencyImpl<String> implements FormatIntegerSequenceDependency {
    public FormatIntegerSequenceDependencyImpl(final String format, final boolean satisfied) {
        super(format, satisfied);
    }
}
