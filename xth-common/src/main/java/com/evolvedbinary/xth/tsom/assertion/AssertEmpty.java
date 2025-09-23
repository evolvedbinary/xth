package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertEmptyImpl;

public sealed interface AssertEmpty extends Assertion permits AssertEmptyImpl {
}
