package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.assertion.impl.AssertErrorCodeImpl;

public interface AssertErrorCode extends AssertError {
    String getCode();
}
