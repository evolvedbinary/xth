package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertEqual;

public final class AssertEqualImpl implements AssertEqual {
    private final String xpathExpression;

    public AssertEqualImpl(final String xpathExpression) {
        this.xpathExpression = xpathExpression;
    }

    @Override
    public String getXpathExpression() {
        return xpathExpression;
    }
}
