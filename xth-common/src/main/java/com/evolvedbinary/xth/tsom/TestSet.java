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
package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.TestSetImpl;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public sealed interface TestSet permits TestSetImpl {
    UUID getTestSetInstanceId();
    String getName();
    Path getFile();
    List<String> getCovers();
    List<String> getCovers30();
    @Nullable String getDescription();
    List<Link> getLinks();
    List<Environment> getEnvironments();
    List<Dependency<?>> getDependencies();

    interface Builder {
        Builder setFile(Path file);
        Builder setCovers(List<String> covers);
        Builder setCovers30(List<String> covers30);
        Builder setDescription(String description);
        Builder addLink(Link link);
        Builder addEnvironment(Environment environment);
        Builder addDependency(Dependency<?> dependency);
        TestSet build();
    }
}
