package com.evolvedbinary.xth.configuration;

import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;

public record Configuration(
    @Nullable Path gitPath,
    URI gitRepoUri,
    String gitBranch,
    Path cacheDirectory,
    Path outputDir,
    boolean verbose
) { }
