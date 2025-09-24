package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.TestImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed interface Test permits TestImpl {
    @Nullable String getContent();
    @Nullable URI getFile();
}
