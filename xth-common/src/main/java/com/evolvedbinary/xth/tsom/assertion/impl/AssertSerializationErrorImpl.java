package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertSerializationError;

public class AssertSerializationErrorImpl implements AssertSerializationError {
    private final String code;

    public AssertSerializationErrorImpl(final String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
