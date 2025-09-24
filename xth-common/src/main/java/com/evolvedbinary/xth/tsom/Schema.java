package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.SchemaImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public sealed interface Schema extends Base permits SchemaImpl {
    @Nullable String getDescription();
    @Nullable Created getCreated();
    List<Modified> getModified();
    URI getUri();
    @Nullable URI getFile();
    XsdVersion getXsdVersion();
    @Nullable Role getRole();
}
