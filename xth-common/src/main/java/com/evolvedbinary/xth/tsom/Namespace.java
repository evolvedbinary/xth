package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

public interface Namespace {
    @Nullable String getPrefix();
    String getUri();
}
