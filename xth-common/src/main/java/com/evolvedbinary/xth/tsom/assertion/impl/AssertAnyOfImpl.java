package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.AssertAnyOf;

import java.util.List;

public class AssertAnyOfImpl extends AbstractSequenceOfAssertions implements AssertAnyOf {
    public AssertAnyOfImpl(final List<Assertion> assertions) {
        super(assertions);
    }
}
