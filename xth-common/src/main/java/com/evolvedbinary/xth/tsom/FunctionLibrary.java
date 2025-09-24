package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.FunctionLibraryImpl;
import org.jspecify.annotations.Nullable;

public sealed interface FunctionLibrary permits FunctionLibraryImpl {
    String getName();
    @Nullable String getXsltLocation();
    @Nullable String getXqueryLocation();
}
