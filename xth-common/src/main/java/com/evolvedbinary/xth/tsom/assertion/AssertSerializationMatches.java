package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertSerializationMatchesImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed interface AssertSerializationMatches extends Assertion permits AssertSerializationMatchesImpl {
    @Nullable URI getFile();
    @Nullable String getRegularExpressionFlags();
    @Nullable String getRegularExpression();
}
