package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertType;

public final class AssertTypeImpl implements AssertType {
    private final String type;

    public AssertTypeImpl(final String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
