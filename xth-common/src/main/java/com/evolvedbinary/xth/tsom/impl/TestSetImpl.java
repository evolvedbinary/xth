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

import com.evolvedbinary.xth.tsom.AbstractBase;
import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.Link;
import com.evolvedbinary.xth.tsom.TestSet;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;

public final class TestSetImpl extends AbstractBase implements TestSet {
    private final UUID testSetInstanceId;
    private final String name;
    private final Path file;
    private final List<String> covers;
    private final List<String> covers30;
    @Nullable private final String description;
    private final List<Link> links;
    private final List<Environment> environments;
    private final List<Dependency<?>> dependencies;

    public TestSetImpl(final UUID testSetInstanceId, final String xmlId, final String name, final Path file,
            final List<String> covers, final List<String> covers30, @Nullable final String description,
            final List<Link> links, final List<Environment> environments, final List<Dependency<?>> dependencies) {
        super(xmlId);
        this.testSetInstanceId = testSetInstanceId;
        this.name = name;
        this.file = file;
        this.covers = covers;
        this.covers30 = covers30;
        this.description = description;
        this.links = links;
        this.environments = environments;
        this.dependencies = dependencies;
    }

    @Override
    public UUID getTestSetInstanceId() {
        return testSetInstanceId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Path getFile() {
        return file;
    }

    @Override
    public List<String> getCovers() {
        return covers;
    }

    @Override
    public List<String> getCovers30() {
        return covers30;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public List<Link> getLinks() {
        return links;
    }

    @Override
    public List<Environment> getEnvironments() {
        return environments;
    }

    @Override
    public List<Dependency<?>> getDependencies() {
        return dependencies;
    }

    public static TestSetImpl.Builder builder(final UUID testSetInstanceId, final String name) {
        return new TestSetImpl.Builder(testSetInstanceId, name);
    }

    public static class Builder extends AbstractBase.Builder implements TestSet.Builder {
        private final UUID testSetInstanceId;
        private final String name;
        private Path file = null;
        private List<String> covers = null;
        private List<String> covers30 = null;
        @Nullable private String description = null;
        private List<Link> links = null;
        private List<Environment> environments = null;
        private List<Dependency<?>> dependencies = null;

        private Builder(final UUID testSetInstanceId, final String name) {
            this.testSetInstanceId = testSetInstanceId;
            this.name = name;
        }

        @Override
        public Builder setFile(final Path file) {
            this.file = file;
            return this;
        }

        @Override
        public Builder setCovers(final List<String> covers) {
            this.covers = covers;
            return this;
        }

        @Override
        public Builder setCovers30(final List<String> covers30) {
            this.covers30 = covers30;
            return this;
        }

        @Override
        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }

        @Override
        public Builder addLink(final Link link) {
            if (links == null) {
                links = new ArrayList<>();
            }
            links.add(link);
            return this;
        }

        @Override
        public Builder addEnvironment(final Environment environment) {
            if (environments == null) {
                environments = new ArrayList<>();
            }
            environments.add(environment);
            return this;
        }

        @Override
        public Builder addDependency(final Dependency<?> dependency) {
            if (dependencies == null) {
                dependencies = new ArrayList<>();
            }
            dependencies.add(dependency);
            return this;
        }

        @Override
        public TestSet build() {
            return new TestSetImpl(
                testSetInstanceId,
                xmlId,
                name,
                file,
                toImmutableList(covers),
                toImmutableList(covers30),
                description,
                toImmutableList(links),
                toImmutableList(environments),
                toImmutableList(dependencies)
            );
        }
    }
}
