package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.FunctionLibrary;
import org.jspecify.annotations.Nullable;

public final class FunctionLibraryImpl implements FunctionLibrary {
    private final String name;
    @Nullable private final String xsltLocation;
    @Nullable private final String xqueryLocation;

    public FunctionLibraryImpl(final String name, @Nullable final String xsltLocation, @Nullable final String xqueryLocation) {
        this.name = name;
        this.xsltLocation = xsltLocation;
        this.xqueryLocation = xqueryLocation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable String getXqueryLocation() {
        return xqueryLocation;
    }

    @Override
    public @Nullable String getXsltLocation() {
        return xsltLocation;
    }
}
