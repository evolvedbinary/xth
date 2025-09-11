package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Namespace;
import org.jspecify.annotations.Nullable;

public class NamespaceImpl implements Namespace {
    @Nullable private final String prefix;
    private final String uri;

    public NamespaceImpl(@Nullable final String prefix, final String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    @Override
    public @Nullable String getPrefix() {
        return prefix;
    }

    @Override
    public String getUri() {
        return uri;
    }
}
