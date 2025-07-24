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
package com.evolvedbinary.xth.parser.api;

import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static com.evolvedbinary.xth.util.SetUtil.safeAdd;

public abstract class AbstractTestSuiteParser implements TestSuiteParser {

    protected @Nullable Set<ParserEventListener> listeners;

    @Override
    public void addEventListener(final ParserEventListener eventListener) {
        listeners = safeAdd(listeners, eventListener, LinkedHashSet::new);
    }

    @Override
    public void removeEventListener(final ParserEventListener eventListener) {
        if (listeners != null) {
            listeners.remove(eventListener);
        }
    }

    public void emitEvent(final Consumer<ParserEventListener> dispatch) {
        if (listeners != null) {
            final Iterator<ParserEventListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final ParserEventListener eventListener = iterator.next();
                dispatch.accept(eventListener);
            }
        }
    }

    protected UUID generateUniqueId() {
        return UUID.randomUUID();
    }
}
