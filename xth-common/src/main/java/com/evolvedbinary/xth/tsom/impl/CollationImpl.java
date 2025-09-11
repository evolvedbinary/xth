package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Collation;

import java.net.URI;

public class CollationImpl implements Collation {

    private final URI uri;
    private final boolean def;

    public CollationImpl(final URI uri, final boolean def) {
        this.uri = uri;
        this.def = def;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public boolean isDefault() {
        return def;
    }
}
