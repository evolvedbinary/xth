package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import java.net.URI;

public interface Test {
    @Nullable String getContent();
    @Nullable URI getFile();
}
