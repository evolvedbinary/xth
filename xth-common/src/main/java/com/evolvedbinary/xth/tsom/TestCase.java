package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import java.util.List;

public interface TestCase {
    String getName();
    List<String> getCovers();
    List<String> getCovers30();
    @Nullable String getDescription();
    @Nullable Created getCreated();
    List<Modified> getModified();
    List<Environment> getEnvironments();
    List<Module> getModules();
    List<Dependency> getDependencies();
    Test getTest();
    Assertion getResult();

    interface Builder {
        void setCovers(List<String> covers);
        void setCovers30(List<String> covers30);
        void setDescription(String description);
        void setCreated(Created created);
        void addModified(Modified modified);
        void addEnvironment(Environment environment);
        void addModule(Module module);
        void addDependency(Dependency dependency);
        void setTest(Test test);
        void setResult(Assertion assertion);
        TestCase build();
    }
}
