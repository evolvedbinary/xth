package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertSerializationMatches;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public class AssertSerializationMatchesImpl implements AssertSerializationMatches {

    @Nullable private final URI file;
    @Nullable private final String regularExpressionFlags;
    @Nullable private final String regularExpression;

    public AssertSerializationMatchesImpl(@Nullable final URI file, @Nullable final String regularExpressionFlags, @Nullable final String regularExpression) {
        this.file = file;
        this.regularExpressionFlags = regularExpressionFlags;
        this.regularExpression = regularExpression;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public @Nullable String getRegularExpressionFlags() {
        return regularExpressionFlags;
    }

    @Override
    public @Nullable String getRegularExpression() {
        return regularExpression;
    }
}
