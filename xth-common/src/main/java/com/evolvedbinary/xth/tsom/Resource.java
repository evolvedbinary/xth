package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.ResourceImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public sealed interface Resource extends Base permits ResourceImpl {
    @Nullable String getDescription();
    @Nullable Created getCreated();
    List<Modified> getModified();
    @Nullable URI getFile();
    URI getUri();
    String getMediaType();
    String getEncoding();
}
