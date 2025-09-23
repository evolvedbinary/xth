package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertXmlImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed interface AssertXml extends Assertion permits AssertXmlImpl {
    @Nullable URI getFile();
    boolean isIgnorePrefixes();
    @Nullable String getXml();
}
