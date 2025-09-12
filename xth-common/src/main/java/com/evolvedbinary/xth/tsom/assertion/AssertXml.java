package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public interface AssertXml extends Assertion {
    @Nullable URI getFile();
    boolean isIgnorePrefixes();
    @Nullable String getXml();
}
