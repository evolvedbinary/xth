package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.AssertNot;

public class AssertNotImpl implements AssertNot {
    private final Assertion assertion;

    public AssertNotImpl(final Assertion assertion) {
        this.assertion = assertion;
    }

    @Override
    public Assertion getAssertion() {
        return assertion;
    }
}
