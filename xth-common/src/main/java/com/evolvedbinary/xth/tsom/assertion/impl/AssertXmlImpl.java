package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertXml;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public final class AssertXmlImpl implements AssertXml {

    @Nullable private final URI file;
    private final boolean ignorePrefixes;
    @Nullable private final String xml;

    public AssertXmlImpl(@Nullable final URI file, final boolean ignorePrefixes, @Nullable final String xml) {
        this.file = file;
        this.ignorePrefixes = ignorePrefixes;
        this.xml = xml;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public boolean isIgnorePrefixes() {
        return ignorePrefixes;
    }

    @Override
    public @Nullable String getXml() {
        return xml;
    }
}
