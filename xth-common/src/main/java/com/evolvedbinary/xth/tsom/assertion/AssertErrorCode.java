package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.assertion.impl.AssertErrorCodeImpl;

public sealed interface AssertErrorCode extends AssertError permits AssertErrorCodeImpl {
    String getCode();
}
