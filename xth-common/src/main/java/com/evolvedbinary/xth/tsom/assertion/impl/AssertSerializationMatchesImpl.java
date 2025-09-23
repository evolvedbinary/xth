package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertSerializationMatches;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public final class AssertSerializationMatchesImpl implements AssertSerializationMatches {

    @Nullable private final URI file;
    private final String regularExpression;
    @Nullable private final String regularExpressionFlags;


    public AssertSerializationMatchesImpl(@Nullable final URI file, @Nullable final String regularExpression, @Nullable final String regularExpressionFlags) {
        this.file = file;
        this.regularExpression = regularExpression;
        this.regularExpressionFlags = regularExpressionFlags;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public @Nullable String getRegularExpression() {
        return regularExpression;
    }

    @Override
    public @Nullable String getRegularExpressionFlags() {
        return regularExpressionFlags;
    }
}
