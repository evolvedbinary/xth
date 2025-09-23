package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertEqualImpl;

public sealed interface AssertEqual extends Assertion permits AssertEqualImpl {
    String getXpathExpression();
}
