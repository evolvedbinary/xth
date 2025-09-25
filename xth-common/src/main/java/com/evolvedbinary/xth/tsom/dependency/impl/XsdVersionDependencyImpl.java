package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.XsdVersionDependency;

public final class XsdVersionDependencyImpl extends AbstractDependencyImpl<String> implements XsdVersionDependency {
    public XsdVersionDependencyImpl(final String xsdVersion, final boolean satisfied) {
        super(xsdVersion, satisfied);
    }
}
