package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertImpl;

public sealed interface Assert extends Assertion permits AssertImpl {
    String getXpathExpression();
}
