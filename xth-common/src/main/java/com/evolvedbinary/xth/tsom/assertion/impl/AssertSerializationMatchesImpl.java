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
package com.evolvedbinary.xth.tsom.assertion.impl;

import com.evolvedbinary.xth.tsom.assertion.AssertSerializationMatches;
import org.jspecify.annotations.Nullable;

import java.net.URI;

public final class AssertSerializationMatchesImpl implements AssertSerializationMatches {

    @Nullable private final URI file;
    private final String regularExpression;
    @Nullable private final String regularExpressionFlags;


    public AssertSerializationMatchesImpl(@Nullable final URI file, @Nullable final String regularExpression, @Nullable final String regularExpressionFlags) {
        this.file = file;
        this.regularExpression = regularExpression;
        this.regularExpressionFlags = regularExpressionFlags;
    }

    @Override
    public @Nullable URI getFile() {
        return file;
    }

    @Override
    public @Nullable String getRegularExpression() {
        return regularExpression;
    }

    @Override
    public @Nullable String getRegularExpressionFlags() {
        return regularExpressionFlags;
    }
}
