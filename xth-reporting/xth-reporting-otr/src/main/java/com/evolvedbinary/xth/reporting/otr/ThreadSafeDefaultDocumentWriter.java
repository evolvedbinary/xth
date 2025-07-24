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
package com.evolvedbinary.xth.reporting.otr;

import net.jcip.annotations.ThreadSafe;
import org.opentest4j.reporting.events.api.Appendable;
import org.opentest4j.reporting.events.api.ChildElement;
import org.opentest4j.reporting.events.api.DocumentWriter;
import org.opentest4j.reporting.events.api.Element;
import org.opentest4j.reporting.events.api.Factory;
import org.opentest4j.reporting.events.api.NamespaceRegistry;
import org.opentest4j.reporting.schema.QualifiedName;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Delegates to org.opentest4j.reporting.events.api.DefaultDocumentWriter
 * but makes sure that both {@link #append(Factory, Consumer)} and {@link #close()}
 * are synchronized.
 */
@ThreadSafe
public class ThreadSafeDefaultDocumentWriter<T extends Element<T>> implements DocumentWriter<T> {

    private final DocumentWriter<T> delegate;

    ThreadSafeDefaultDocumentWriter(final DocumentWriter<T> delegate) {
        this.delegate = delegate;
    }

    static <R extends Element<R>> DocumentWriter<R> create(QualifiedName rootElementName, NamespaceRegistry namespaceRegistry, Path xmlFile) throws Exception {
        final DocumentWriter<R> delegate = DocumentWriter.create(rootElementName, namespaceRegistry, Files.newBufferedWriter(xmlFile));
        return new ThreadSafeDefaultDocumentWriter<>(delegate);
    }

    static <R extends Element<R>> DocumentWriter<R> create(final QualifiedName rootElementName, final NamespaceRegistry namespaceRegistry, final Writer writer) throws Exception {
        final DocumentWriter<R> delegate = DocumentWriter.create(rootElementName, namespaceRegistry, writer);
        return new ThreadSafeDefaultDocumentWriter<>(delegate);
    }

    @Override
    public synchronized <C extends ChildElement<T, ? super C>> Appendable<T> append(Factory<C> creator, Consumer<? super C> configurer) {
        return delegate.append(creator, configurer);
    }

    @Override
    public synchronized void close() throws IOException {
        delegate.close();
    }
}
