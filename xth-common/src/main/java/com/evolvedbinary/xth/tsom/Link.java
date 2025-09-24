package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.LinkImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed interface Link permits LinkImpl {
    DependencyType getType();
    @Nullable String getDocument();
    @Nullable String getIdRef();
    @Nullable String getSectionNumber();
}
