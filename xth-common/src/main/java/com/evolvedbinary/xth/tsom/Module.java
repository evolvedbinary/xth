package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import java.net.URI;

public interface Module {
    String getUri();
    @Nullable URI getLocation();
    URI getFile();
}
