package com.evolvedbinary.xth.cli;

//import com.evolvedbinary.xth.configuration.Feature;
//import com.evolvedbinary.xth.configuration.Specification;
//import com.evolvedbinary.xth.configuration.XmlVersion;
//import com.evolvedbinary.xth.configuration.XsdVersion;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;
//import java.util.EnumSet;
//import java.util.List;

@NullMarked
public class Arguments {
    @Nullable private Path gitPath;
    @Nullable private URI qtGitRepoUri;
    @Nullable private String qtGitBranch;
    @Nullable private Path cacheDirectory;
    @Nullable private Path outputDirectory;
    private boolean verbose;

//    @Nullable String testSetPattern,
//    @Nullable List<String> testSets,
//    @Nullable List<String> excludeTestSets,
//    @Nullable String testCasePattern,
//    @Nullable List<String> testCases,
//    @Nullable List<String> excludeTestCases,
//    @Nullable EnumSet<Feature> enableFeatures,
//    @Nullable EnumSet<Feature> disableFeatures,
//    @Nullable EnumSet<Specification> enableSpecifications,
//    @Nullable EnumSet<Specification> disableSpecifications,
//    @Nullable EnumSet<XmlVersion> enableXmlVersions,
//    @Nullable EnumSet<XmlVersion> disableXmlVersions,
//    @Nullable EnumSet<XsdVersion> enableXsdVersions,
//    @Nullable EnumSet<XsdVersion> disableXsdVersions


    public @Nullable Path getGitPath() {
        return gitPath;
    }

    public void setGitPath(@Nullable final Path gitPath) {
        this.gitPath = gitPath;
    }

    public @Nullable URI getQtGitRepoUri() {
        return qtGitRepoUri;
    }

    public void setQtGitRepoUri(@Nullable final URI qtGitRepoUri) {
        this.qtGitRepoUri = qtGitRepoUri;
    }

    public @Nullable String getQtGitBranch() {
        return qtGitBranch;
    }

    public void setQtGitBranch(@Nullable final String qtGitBranch) {
        this.qtGitBranch = qtGitBranch;
    }

    public @Nullable Path getCacheDirectory() {
        return cacheDirectory;
    }

    public void setCacheDirectory(@Nullable final Path cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }

    public @Nullable Path getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(@Nullable final Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }
}
