package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.SourceImpl;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public sealed interface Source extends Base permits SourceImpl {
    @Nullable String getDescription();
    @Nullable Created getCreated();
    List<Modified> getModified();
    @Nullable Role getRole();
    @Nullable URI getFile();
    URI getUri();
    ValidationMode getValidationMode();
}
