package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.AbstractBase;
import com.evolvedbinary.xth.tsom.Created;
import com.evolvedbinary.xth.tsom.Modified;
import com.evolvedbinary.xth.tsom.Resource;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public class ResourceImpl extends AbstractBase implements Resource {
    @Nullable private final String description;
    @Nullable private final Created created;
    private final List<Modified> modified;
    @Nullable private final URI file;
    private final URI uri;
    private final String mediaType;
    private final String encoding;

    public ResourceImpl(@Nullable final String xmlId, @Nullable final String description, @Nullable final Created created, final List<Modified> modified, @Nullable final URI file, final URI uri, final String mediaType, final String encoding) {
        super(xmlId);
        this.description = description;
        this.created = created;
        this.modified = modified;
        this.file = file;
        this.uri = uri;
        this.mediaType = mediaType;
        this.encoding = encoding;
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
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }
}
