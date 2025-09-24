package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.DependencyType;
import com.evolvedbinary.xth.tsom.Link;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public final class LinkImpl implements Link {
    private final DependencyType type;
    @Nullable private final String document;
    @Nullable private final String idRef;
    @Nullable private final String sectionNumber;

    public LinkImpl(final DependencyType type, @Nullable final String document, @Nullable final String idRef, @Nullable final String sectionNumber) {
        this.type = type;
        this.document = document;
        this.idRef = idRef;
        this.sectionNumber = sectionNumber;
    }

    @Override
    public DependencyType getType() {
        return type;
    }

    @Override
    public @Nullable String getDocument() {
        return document;
    }

    @Override
    public @Nullable String getIdRef() {
        return idRef;
    }

    @Override
    public @Nullable String getSectionNumber() {
        return sectionNumber;
    }
}
