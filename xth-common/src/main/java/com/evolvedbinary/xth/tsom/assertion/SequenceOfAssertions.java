package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AbstractSequenceOfAssertions;

import java.util.List;

public sealed interface SequenceOfAssertions extends Assertion permits AssertAnyOf, AssertAllOf, AbstractSequenceOfAssertions {
    List<Assertion> getAssertions();
}
