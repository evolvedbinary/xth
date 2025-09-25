package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.XmlVersionDependency;

public final class XmlVersionDependencyImpl extends AbstractDependencyImpl<String> implements XmlVersionDependency {
    public XmlVersionDependencyImpl(final String xmlVersion, final boolean satisfied) {
        super(xmlVersion, satisfied);
    }
}
