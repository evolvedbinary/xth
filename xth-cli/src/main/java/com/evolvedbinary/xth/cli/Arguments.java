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
package com.evolvedbinary.xth.cli;

//import com.evolvedbinary.xth.configuration.Feature;
//import com.evolvedbinary.xth.configuration.Specification;
//import com.evolvedbinary.xth.configuration.XmlVersion;
//import com.evolvedbinary.xth.configuration.XsdVersion;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;
import java.util.regex.Pattern;

@NullMarked
public class Arguments {
    @Nullable private Path gitPath = null;
    @Nullable private URI qtGitRepoUri = null;
    @Nullable private String qtGitBranch = null;
    @Nullable private Path cacheDirectory = null;
    @Nullable private Path outputDirectory = null;
    @Nullable private Pattern testSetPattern = null;
    @Nullable private String[] testSets = null;
    @Nullable private String[] excludeTestSets = null;
    @Nullable private String[] skipTestSets = null;
    @Nullable private Pattern testCasePattern = null;
    @Nullable private String[] testCases = null;
    @Nullable private String[] excludeTestCases = null;
    @Nullable private String[] skipTestCases = null;
    private boolean offlineMode;
    private boolean verbose;
    private boolean showHelp;

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

    public @Nullable Pattern getTestSetPattern() {
        return testSetPattern;
    }

    public void setTestSetPattern(@Nullable final Pattern testSetPattern) {
        this.testSetPattern = testSetPattern;
    }

    public @Nullable String[] getTestSets() {
        return testSets;
    }

    public void setTestSets(@Nullable final String[] testSets) {
        this.testSets = testSets;
    }

    public @Nullable String[] getExcludeTestSets() {
        return excludeTestSets;
    }

    public void setExcludeTestSets(@Nullable final String[] excludeTestSets) {
        this.excludeTestSets = excludeTestSets;
    }

    public @Nullable String[] getSkipTestSets() {
        return skipTestSets;
    }

    public void setSkipTestSets(@Nullable final String[] skipTestSets) {
        this.skipTestSets = skipTestSets;
    }

    public @Nullable Pattern getTestCasePattern() {
        return testCasePattern;
    }

    public void setTestCasePattern(@Nullable final Pattern testCasePattern) {
        this.testCasePattern = testCasePattern;
    }

    public @Nullable String[] getTestCases() {
        return testCases;
    }

    public void setTestCases(@Nullable final String[] testCases) {
        this.testCases = testCases;
    }

    public @Nullable String[] getExcludeTestCases() {
        return excludeTestCases;
    }

    public void setExcludeTestCases(@Nullable final String[] excludeTestCases) {
        this.excludeTestCases = excludeTestCases;
    }

    public @Nullable String[] getSkipTestCases() {
        return skipTestCases;
    }

    public void setSkipTestCases(@Nullable final String[] skipTestCases) {
        this.skipTestCases = skipTestCases;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(final boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(final boolean showHelp) {
        this.showHelp = showHelp;
    }
}
