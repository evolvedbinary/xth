package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;

public sealed interface AssertError extends Assertion permits AssertAnyError, AssertErrorCode {
}
