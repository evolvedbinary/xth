package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.*;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;

public class TestSetImpl extends AbstractBase implements TestSet {
    private final String name;
    private final Path file;
    private final List<String> covers;
    private final List<String> covers30;
    @Nullable private final String description;
    private final List<Link> links;
    private final List<Environment> environments;
    private final List<Dependency> dependencies;

    public TestSetImpl(final String xmlId, final String name, final Path file, final List<String> covers,
            final List<String> covers30, @Nullable final String description, final List<Link> links,
            final List<Environment> environments, final List<Dependency> dependencies) {
        super(xmlId);
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
    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public static TestSetImpl.Builder builder(final String name) {
        return new TestSetImpl.Builder(name);
    }

    public static class Builder extends AbstractBase.Builder implements TestSet.Builder {
        private final String name;
        private Path file = null;
        private List<String> covers = null;
        private List<String> covers30 = null;
        @Nullable private String description = null;
        private List<Link> links = null;
        private List<Environment> environments = null;
        private List<Dependency> dependencies = null;

        private Builder(final String name) {
            this.name = name;
        }

        @Override
        public void setFile(final Path file) {
            this.file = file;
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
        public void addLink(final Link link) {
            if (links == null) {
                links = new ArrayList<>();
            }
            links.add(link);
        }

        @Override
        public void addEnvironment(final Environment environment) {
            if (environments == null) {
                environments = new ArrayList<>();
            }
            environments.add(environment);
        }

        @Override
        public void addDependency(final Dependency dependency) {
            if (dependencies == null) {
                dependencies = new ArrayList<>();
            }
            dependencies.add(dependency);
        }

        @Override
        public TestSet build() {
            return new TestSetImpl(
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
