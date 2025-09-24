package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public interface TestSet {
    String getName();
    Path getFile();
    List<String> getCovers();
    List<String> getCovers30();
    @Nullable String getDescription();
    List<Link> getLinks();
    List<Environment> getEnvironments();
    List<Dependency> getDependencies();

    interface Builder {
        void setFile(Path file);
        void setCovers(List<String> covers);
        void setCovers30(List<String> covers30);
        void setDescription(String description);
        void addLink(Link link);
        void addEnvironment(Environment environment);
        void addDependency(Dependency dependency);
        TestSet build();
    }
}
