package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

public interface FunctionLibrary {
    String getName();
    @Nullable String getXsltLocation();
    @Nullable String getXqueryLocation();
}
