package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public interface Source extends Base {
    @Nullable String getDescription();
    @Nullable Created getCreated();
    List<Modified> getModified();
    @Nullable Role getRole();
    @Nullable URI getFile();
    URI getUri();
    ValidationMode getValidationMode();
}
