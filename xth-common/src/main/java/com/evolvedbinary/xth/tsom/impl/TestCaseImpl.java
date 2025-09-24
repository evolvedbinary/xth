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

import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;

public final class TestCaseImpl implements TestCase {
    private final String name;
    private final List<String> covers;
    private final List<String> covers30;
    @Nullable private final String description;
    @Nullable private final Created created;
    private final List<Modified> modified;
    private final List<Environment> environments;
    private final List<Module> modules;
    private final List<Dependency> dependencies;
    private final Test test;
    private final Assertion result;

    public TestCaseImpl(final String name, final List<String> covers, final List<String> covers30,
            @Nullable final String description, @Nullable final Created created, final List<Modified> modified,
            final List<Environment> environments, final List<Module> modules, final List<Dependency> dependencies,
            final Test test, final Assertion result) {
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
    public List<Dependency> getDependencies() {
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


    public static Builder builder(final String name) {
        return new TestCaseImpl.Builder(name);
    }

    public static class Builder implements TestCase.Builder {
        private final String name;
        private List<String> covers = null;
        private List<String> covers30 = null;
        @Nullable private String description = null;
        @Nullable private Created created = null;
        private List<Modified> modified = null;
        private List<Environment> environments = null;
        private List<Module> modules = null;
        private List<Dependency> dependencies = null;
        private Test test = null;
        private Assertion result = null;

        private Builder(final String name) {
            this.name = name;
        }

        @Override
        public void setCovers(final List<String> covers) {
            this.covers = covers;
        }

        @Override
        public void setCovers30(final List<String> covers30) {
            this.covers30 = covers30;
        }

        @Override
        public void setDescription(final String description) {
            this.description = description;
        }

        @Override
        public void setCreated(final Created created) {
            this.created = created;
        }

        @Override
        public void addModified(final Modified modified) {
            if (this.modified == null) {
                this.modified = new ArrayList<>();
            }
            this.modified.add(modified);
        }

        @Override
        public void addEnvironment(final Environment environment) {
            if (this.environments == null) {
                this.environments = new ArrayList<>();
            }
            this.environments.add(environment);
        }

        @Override
        public void addModule(final Module module) {
            if (this.modules == null) {
                this.modules = new ArrayList<>();
            }
            this.modules.add(module);
        }

        @Override
        public void addDependency(final Dependency dependency) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList<>();
            }
            this.dependencies.add(dependency);
        }

        @Override
        public void setTest(final Test test) {
            this.test = test;
        }

        @Override
        public void setResult(final Assertion result) {
            this.result = result;
        }

        @Override
        public TestCase build() {
            return new TestCaseImpl(
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
