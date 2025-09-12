package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertStringValue;

public class AssertStringValueImpl implements AssertStringValue {
    private final String stringValue;
    private final boolean normalizeSpace;

    public AssertStringValueImpl(final String stringValue, final boolean normalizeSpace) {
        this.stringValue = stringValue;
        this.normalizeSpace = normalizeSpace;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }

    @Override
    public boolean isNormalizeSpace() {
        return normalizeSpace;
    }
}
