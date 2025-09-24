package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.ModuleImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed interface Module permits ModuleImpl {
    String getUri();
    @Nullable URI getLocation();
    URI getFile();
}
