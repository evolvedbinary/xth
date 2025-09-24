package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.StaticBaseUri;

import javax.xml.namespace.QName;
import java.net.URI;

public final class StaticBaseUriImpl implements StaticBaseUri {
    private final URI uri;

    public StaticBaseUriImpl(final URI uri) {
        this.uri = uri;
    }

    @Override
    public URI getUri() {
        return uri;
    }
}
