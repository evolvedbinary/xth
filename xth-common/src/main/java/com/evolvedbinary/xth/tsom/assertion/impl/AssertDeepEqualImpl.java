package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertDeepEqual;

public class AssertDeepEqualImpl implements AssertDeepEqual {
    private final String sequence;

    public AssertDeepEqualImpl(final String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String getSequence() {
        return sequence;
    }
}
