package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.AbstractBase;
import com.evolvedbinary.xth.tsom.Created;
import com.evolvedbinary.xth.tsom.Modified;
import com.evolvedbinary.xth.tsom.Role;
import com.evolvedbinary.xth.tsom.Source;
import com.evolvedbinary.xth.tsom.ValidationMode;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public final class SourceImpl extends AbstractBase implements Source {
    @Nullable private final String description;
    @Nullable private final Created created;
    private final List<Modified> modified;
    @Nullable private final Role role;
    @Nullable private final URI file;
    private final URI uri;
    private final ValidationMode validationMode;

    public SourceImpl(@Nullable final String xmlId, @Nullable final String description, @Nullable final Created created, final List<Modified> modified, @Nullable final Role role, @Nullable final URI file, final URI uri, final ValidationMode validationMode) {
        super(xmlId);
        this.description = description;
        this.created = created;
        this.modified = modified;
        this.role = role;
        this.file = file;
        this.uri = uri;
        this.validationMode = validationMode;
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
    public @Nullable Role getRole() {
        return role;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public ValidationMode getValidationMode() {
        return validationMode;
    }
}
