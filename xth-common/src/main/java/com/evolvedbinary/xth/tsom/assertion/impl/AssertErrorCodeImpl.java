package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertErrorCode;

public final class AssertErrorCodeImpl implements AssertErrorCode {
    private final String code;

    public AssertErrorCodeImpl(final String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
