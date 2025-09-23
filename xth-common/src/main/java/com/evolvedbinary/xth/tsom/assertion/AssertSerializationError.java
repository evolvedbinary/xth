package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertSerializationErrorImpl;

public sealed interface AssertSerializationError extends Assertion permits AssertSerializationErrorImpl {
    String getCode();
}
