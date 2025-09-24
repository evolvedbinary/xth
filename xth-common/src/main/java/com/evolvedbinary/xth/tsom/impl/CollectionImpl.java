package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Collection;
import com.evolvedbinary.xth.tsom.Resource;
import com.evolvedbinary.xth.tsom.Source;

import java.net.URI;
import java.util.List;

public final class CollectionImpl implements Collection {
    private final URI uri;
    private final List<Source> source;
    private final List<Resource> resource;
    private final List<String> query;

    public CollectionImpl(final URI uri, final List<Source> source, final List<Resource> resource, final List<String> query) {
        this.uri = uri;
        this.source = source;
        this.resource = resource;
        this.query = query;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public List<Source> getSource() {
        return source;
    }

    @Override
    public List<Resource> getResource() {
        return resource;
    }

    @Override
    public List<String> getQuery() {
        return query;
    }
}
