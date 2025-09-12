package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public interface AssertSerializationMatches extends Assertion {
    @Nullable URI getFile();
    @Nullable String getRegularExpressionFlags();
    @Nullable String getRegularExpression();
}
