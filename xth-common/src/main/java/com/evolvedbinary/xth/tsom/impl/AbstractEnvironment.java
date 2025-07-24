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

import com.evolvedbinary.xth.tsom.Environment;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public sealed abstract class AbstractEnvironment implements Environment permits EnvironmentReferenceImpl, EnvironmentDefinitionImpl {

    private final URI baseUri;
    @Nullable private final String name;

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    @Override
    public @Nullable String getName() {
        return name;
    }

    protected AbstractEnvironment(final URI baseUri, @Nullable final String name) {
        this.baseUri = baseUri;
        this.name = name;
    }

    public abstract static sealed class Builder implements Environment.Builder permits EnvironmentReferenceImpl.Builder, EnvironmentDefinitionImpl.Builder {
        protected final URI baseUri;
        protected final String name;

        protected Builder(final URI baseUri, final String name) {
            this.baseUri = baseUri;
            this.name = name;
        }
    }
}
