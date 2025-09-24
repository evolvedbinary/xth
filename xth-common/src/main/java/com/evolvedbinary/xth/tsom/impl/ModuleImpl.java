package com.evolvedbinary.xth.tsom.impl;

import org.jspecify.annotations.Nullable;
import com.evolvedbinary.xth.tsom.Module;

import java.net.URI;

public final class ModuleImpl implements Module {
    private final String uri;
    @Nullable private final URI location;
    private final URI file;

    public ModuleImpl(final String uri,  @Nullable final URI location, final URI file) {
        this.uri = uri;
        this.location = location;
        this.file = file;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public @Nullable URI getLocation() {
        return location;
    }

    @Override
    public URI getFile() {
        return file;
    }
}
