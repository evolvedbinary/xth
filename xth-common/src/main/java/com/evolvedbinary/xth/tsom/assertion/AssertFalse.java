package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertFalseImpl;

public sealed interface AssertFalse extends Assertion permits AssertFalseImpl {
}
