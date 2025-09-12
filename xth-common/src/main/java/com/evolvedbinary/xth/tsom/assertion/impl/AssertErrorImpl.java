package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertError;

public class AssertErrorImpl implements AssertError {
    private final String code;

    public AssertErrorImpl(final String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
