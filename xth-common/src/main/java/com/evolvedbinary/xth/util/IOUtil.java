package com.evolvedbinary.xth.util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface IOUtil {

    static Path path(final URI uri) {
        return Paths.get(uri);
    }

    static Path path(final URI baseUri, final URI uri) {
        if (uri.isAbsolute()) {
            return Paths.get(uri);
        }
        return Paths.get(baseUri.resolve(uri));
    }

    static Path path(final Path baseUri, final URI uri) {
        if (uri.isAbsolute()) {
            return Paths.get(uri);
        }
        return baseUri.resolve(uri.toString());
    }

    static Path path(final String path) {
        return Paths.get(path);
    }

    static Path path(final URI baseUri, final String path) {
        final Path p = Paths.get(path);
        if (p.isAbsolute()) {
            return p;
        }
        return Paths.get(baseUri.resolve(path));
    }

    static Path path(final Path baseUri, final String path) {
        final Path p = Paths.get(path);
        if (p.isAbsolute()) {
            return p;
        }
        return baseUri.resolve(p);
    }

    static String readFileContent(final URI fileUri) throws IOException {
        return readFileContent(path(fileUri));
    }

    static String readFileContent(final Path path) throws IOException {
        return Files.readString(path);
    }
}
