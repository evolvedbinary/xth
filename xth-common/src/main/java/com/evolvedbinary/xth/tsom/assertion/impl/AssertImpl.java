package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.Assert;

public final class AssertImpl implements Assert {
    final String xpathExpression;

    public AssertImpl(final String xpathExpression) {
        this.xpathExpression = xpathExpression;
    }

    @Override
    public String getXpathExpression() {
        return xpathExpression;
    }
}
