package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

public abstract class AbstractBase implements Base {

    @Nullable
    private final String xmlId;

    protected AbstractBase(final String xmlId) {
        this.xmlId = xmlId;
    }

    @Override
    public @Nullable String getXmlId() {
        return xmlId;
    }
}
