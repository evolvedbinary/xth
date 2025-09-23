package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertNotImpl;

public sealed interface AssertNot extends Assertion permits AssertNotImpl {
    Assertion getAssertion();
}
