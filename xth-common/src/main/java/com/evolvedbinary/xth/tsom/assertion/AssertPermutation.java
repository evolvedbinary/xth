package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertPermutationImpl;

public sealed interface AssertPermutation extends Assertion permits AssertPermutationImpl {
    String getSequence();
}
