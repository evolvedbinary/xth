package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.SequenceOfAssertions;

import java.util.List;

public sealed abstract class AbstractSequenceOfAssertions implements SequenceOfAssertions permits AssertAnyOfImpl, AssertAllOfImpl {
    private final List<Assertion> assertions;

    protected AbstractSequenceOfAssertions(final List<Assertion> assertions) {
        this.assertions = assertions;
    }

    @Override
    public List<Assertion> getAssertions() {
        return assertions;
    }
}
