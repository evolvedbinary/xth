package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertStringValueImpl;

public sealed interface AssertStringValue extends Assertion permits AssertStringValueImpl {
    String getStringValue();
    boolean isNormalizeSpace();
}
