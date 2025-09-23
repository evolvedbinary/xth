package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertCountImpl;

import java.math.BigInteger;

public sealed interface AssertCount extends Assertion permits AssertCountImpl {
    BigInteger getCount();
}
