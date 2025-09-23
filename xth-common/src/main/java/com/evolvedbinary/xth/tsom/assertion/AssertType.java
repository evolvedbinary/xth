package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertTypeImpl;

public sealed interface AssertType extends Assertion permits AssertTypeImpl {
    String getType();
}
