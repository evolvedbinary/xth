package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertPermutation;

public class AssertPermutationImpl implements AssertPermutation {
    private final String sequence;

    public AssertPermutationImpl(final String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String getSequence() {
        return sequence;
    }
}
