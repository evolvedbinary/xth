package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import java.net.URI;

public interface Link {
    DependencyType getType();
    @Nullable String getDocument();
    @Nullable String getIdRef();
    @Nullable String getSectionNumber();
}
