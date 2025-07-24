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
package com.evolvedbinary.xth.util;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    final class NullWriter extends Writer {
        private boolean closed;

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
            throwIfClosed();
        }

        @Override
        public void flush() throws IOException {
            throwIfClosed();
        }

        private void throwIfClosed() throws IOException {
            if (closed) {
                throw new IOException("NullWriter is closed");
            }
        }

        @Override
        public void close() {
            closed = true;
        }
    }
}
