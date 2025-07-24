/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.AbstractBase;
import com.evolvedbinary.xth.tsom.Created;
import com.evolvedbinary.xth.tsom.Modified;
import com.evolvedbinary.xth.tsom.Resource;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public final class ResourceImpl extends AbstractBase implements Resource {
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
