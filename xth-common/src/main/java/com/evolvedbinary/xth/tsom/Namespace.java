package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.NamespaceImpl;
import org.jspecify.annotations.Nullable;

public sealed interface Namespace permits NamespaceImpl {
    @Nullable String getPrefix();
    String getUri();
}
