package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertDeepEqualImpl;

public sealed interface AssertDeepEqual extends Assertion permits AssertDeepEqualImpl {
    String getSequence();
}
