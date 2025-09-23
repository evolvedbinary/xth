package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertTrueImpl;

public sealed interface AssertTrue extends Assertion permits AssertTrueImpl {
}
