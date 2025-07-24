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

import com.evolvedbinary.xth.tsom.Assertion;
import com.evolvedbinary.xth.tsom.Created;
import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.Environment;
import com.evolvedbinary.xth.tsom.Modified;
import com.evolvedbinary.xth.tsom.Module;
import com.evolvedbinary.xth.tsom.Test;
import com.evolvedbinary.xth.tsom.TestCase;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;

public final class TestCaseImpl implements TestCase {
    private final UUID testCaseInstanceId;
    private final String name;
    private final List<String> covers;
    private final List<String> covers30;
    @Nullable private final String description;
    @Nullable private final Created created;
    private final List<Modified> modified;
    private final List<Environment> environments;
    private final List<Module> modules;
    private final List<Dependency<?>> dependencies;
    private final Test test;
    private final Assertion result;

    public TestCaseImpl(final UUID testCaseInstanceId, final String name, final List<String> covers,
            final List<String> covers30, @Nullable final String description, @Nullable final Created created,
            final List<Modified> modified, final List<Environment> environments, final List<Module> modules,
            final List<Dependency<?>> dependencies, final Test test, final Assertion result) {
        this.testCaseInstanceId = testCaseInstanceId;
        this.name = name;
        this.covers = covers;
        this.covers30 = covers30;
        this.description = description;
        this.created = created;
        this.modified = modified;
        this.environments = environments;
        this.modules = modules;
        this.dependencies = dependencies;
        this.test = test;
        this.result = result;
    }

    @Override
    public UUID getTestCaseInstanceId() {
        return testCaseInstanceId;
    }

    @Override
    public String getName() {
        return name;
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
    public @Nullable Created getCreated() {
        return created;
    }

    @Override
    public List<Modified> getModified() {
        return modified;
    }

    @Override
    public List<Environment> getEnvironments() {
        return environments;
    }

    @Override
    public List<Module> getModules() {
        return modules;
    }

    @Override
    public List<Dependency<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public Test getTest() {
        return test;
    }

    @Override
    public Assertion getResult() {
        return result;
    }


    public static Builder builder(final UUID testCaseInstanceId, final String name) {
        return new TestCaseImpl.Builder(testCaseInstanceId, name);
    }

    public static class Builder implements TestCase.Builder {
        private final UUID testCaseInstanceId;
        private final String name;
        private List<String> covers = null;
        private List<String> covers30 = null;
        @Nullable private String description = null;
        @Nullable private Created created = null;
        private List<Modified> modified = null;
        private List<Environment> environments = null;
        private List<Module> modules = null;
        private List<Dependency<?>> dependencies = null;
        private Test test = null;
        private Assertion result = null;

        private Builder(final UUID testCaseInstanceId, final String name) {
            this.testCaseInstanceId = testCaseInstanceId;
            this.name = name;
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
        public Builder setCreated(final Created created) {
            this.created = created;
            return this;
        }

        @Override
        public Builder addModified(final Modified modified) {
            if (this.modified == null) {
                this.modified = new ArrayList<>();
            }
            this.modified.add(modified);
            return this;
        }

        @Override
        public Builder addEnvironment(final Environment environment) {
            if (this.environments == null) {
                this.environments = new ArrayList<>();
            }
            this.environments.add(environment);
            return this;
        }

        @Override
        public Builder addModule(final Module module) {
            if (this.modules == null) {
                this.modules = new ArrayList<>();
            }
            this.modules.add(module);
            return this;
        }

        @Override
        public Builder addDependency(final Dependency<?> dependency) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList<>();
            }
            this.dependencies.add(dependency);
            return this;
        }

        @Override
        public Builder setTest(final Test test) {
            this.test = test;
            return this;
        }

        @Override
        public Builder setResult(final Assertion result) {
            this.result = result;
            return this;
        }

        @Override
        public TestCase build() {
            return new TestCaseImpl(
                testCaseInstanceId,
                name,
                toImmutableList(covers),
                toImmutableList(covers30),
                description,
                created,
                toImmutableList(modified),
                toImmutableList(environments),
                toImmutableList(modules),
                toImmutableList(dependencies),
                test,
                result
            );
        }
    }
}
