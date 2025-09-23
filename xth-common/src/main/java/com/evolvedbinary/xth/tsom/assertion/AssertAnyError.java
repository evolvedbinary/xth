package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.assertion.impl.AssertAnyErrorImpl;

public sealed interface AssertAnyError extends AssertError permits AssertAnyErrorImpl {
}
