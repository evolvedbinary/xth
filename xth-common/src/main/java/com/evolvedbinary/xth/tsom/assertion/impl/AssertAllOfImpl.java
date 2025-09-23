package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.AssertAllOf;

import java.util.List;

public final class AssertAllOfImpl extends AbstractSequenceOfAssertions implements AssertAllOf {
    public AssertAllOfImpl(final List<Assertion> assertions) {
        super(assertions);
    }
}
