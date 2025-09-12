package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Test;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public class TestImpl implements Test {
    @Nullable private final String content;
    @Nullable private final URI file;

    public TestImpl(@Nullable final String content, @Nullable final URI file) {
        this.content = content;
        this.file = file;
    }

    @Override
    public @Nullable String getContent() {
        return content;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }
}
