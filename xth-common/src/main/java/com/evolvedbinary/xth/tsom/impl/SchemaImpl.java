package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.AbstractBase;
import com.evolvedbinary.xth.tsom.Created;
import com.evolvedbinary.xth.tsom.Modified;
import com.evolvedbinary.xth.tsom.Role;
import com.evolvedbinary.xth.tsom.Schema;
import com.evolvedbinary.xth.tsom.XsdVersion;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public final class SchemaImpl extends AbstractBase implements Schema {

    @Nullable private final String description;
    @Nullable private final Created created;
    private final List<Modified> modified;
    private final URI uri;
    @Nullable private final URI file;
    private final XsdVersion xsdVersion;
    @Nullable private final Role role;

    public SchemaImpl(@Nullable final String xmlId, @Nullable final String description, @Nullable final Created created,
            final List<Modified> modified, final URI uri, @Nullable final URI file, final XsdVersion xsdVersion,
            @Nullable final Role role) {
        super(xmlId);
        this.description = description;
        this.created = created;
        this.modified = modified;
        this.uri = uri;
        this.file = file;
        this.xsdVersion = xsdVersion;
        this.role = role;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public @Nullable Created getCreated() {
        return created;
    }

    @Override
    public List<Modified> getModified() {
        return modified;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public XsdVersion getXsdVersion() {
        return xsdVersion;
    }

    @Override
    public @Nullable Role getRole() {
        return role;
    }
}
