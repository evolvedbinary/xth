package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertCount;

import java.math.BigInteger;

public class AssertCountImpl implements AssertCount {
    private final BigInteger count;

    public AssertCountImpl(final BigInteger count) {
        this.count = count;
    }

    @Override
    public BigInteger getCount() {
        return count;
    }
}
