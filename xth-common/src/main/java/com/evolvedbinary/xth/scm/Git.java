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
package com.evolvedbinary.xth.scm;

import org.jspecify.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Git {

    public static Path cloneOrUpdateRepository(@Nullable final Path gitPath, final Path cacheDir, final URI gitRepoUri, final String gitBranch, final PrintStream out) throws IllegalArgumentException, IOException {
        final Path repoDir = getRepoDir(cacheDir, gitRepoUri);

        if (Files.exists(repoDir)) {
            if (isExistingRepo(repoDir, gitPath)) {
                // fetch
                fetchForRepository(repoDir, gitPath, out);
            } else {
                // error
                throw new IOException("Path already exists but is not a valid Git repository: " + repoDir);
            }
        } else {
            final Path repoDirParent = repoDir.getParent();
            if (!Files.exists(repoDirParent)) {
                Files.createDirectories(repoDirParent);
            }

            // clone
            cloneRepository(repoDir, gitPath, gitRepoUri, gitBranch, out);
        }

        return repoDir;
    }

    public static Path assertExistingRepository(@Nullable final Path gitPath, final Path cacheDir, final URI gitRepoUri, final String gitBranch) throws IOException {
        final Path repoDir = getRepoDir(cacheDir, gitRepoUri);

        if (Files.exists(repoDir)) {
            if (isExistingRepo(repoDir, gitPath)) {
                // OK
                // TODO(AR) check the repo is on the correct branch
                return repoDir;
            } else {
                // error
                throw new IOException("Path already exists but is not a valid Git repository: " + repoDir);
            }
        } else {
            // error
            throw new IOException("Path to Git repository does not exist: " + repoDir);
        }
    }

    private static boolean isExistingRepo(final Path repoDir, @Nullable final Path gitPath) throws IOException {
        if (!Files.exists(repoDir.resolve(".git"))) {
            return false;
        }

        try {
            final Process process = new ProcessBuilder(getGitBin(gitPath), "status")
                .directory(repoDir.toFile())
                .start();

            if (process.waitFor(5, TimeUnit.SECONDS)) {
                return process.exitValue() == 0;
            } else {
                throw new IOException("Executing `git status` (in: " + repoDir + ") did not complete within 5 seconds");
            }
        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new IOException(e.getMessage(), e);
        }
    }

    private static String getGitBin(@Nullable final Path gitPath) {
        if (gitPath != null) {
            return gitPath.toAbsolutePath().toString();
        } else {
            // default to trying to find it on the OS's PATH
            return "git";
        }
    }

    private static Path getRepoDir(final Path cacheDir, final URI gitRepoUri) throws IllegalArgumentException {
        Path repoDir = cacheDir.resolve("repository");

        final String[] repoPathParts = getRepoPathParts(gitRepoUri);
        for (final String repoPathPart : repoPathParts) {
            repoDir = repoDir.resolve(repoPathPart);
        }

        return repoDir;
    }

    static String[] getRepoPathParts(final URI gitRepoUri) throws IllegalArgumentException {
        String path = gitRepoUri.getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.endsWith(".git")) {
            path = path.substring(0, path.length() - 4);
        }

        if (path.isEmpty()) {
            throw new IllegalArgumentException("Invalid path in URI: " + gitRepoUri);
        }

        return path.split("/");
    }

    static String getRepoName(final URI gitRepoUri) throws IllegalArgumentException {
        String path = gitRepoUri.getPath();
        int idx = path.lastIndexOf('/');
        if (idx == -1) {
            throw new IllegalArgumentException("Path missing from URI: " + gitRepoUri);
        }

        if (idx == path.length() - 1) {
            path = path.substring(0, idx);
            idx = path.lastIndexOf('/');
            if (idx == -1 || idx == path.length() - 1) {
                throw new IllegalArgumentException("Invalid path in URI: " + gitRepoUri);
            }
        }

        path = path.substring(idx + 1);
        if (path.endsWith(".git")) {
            path = path.substring(0, path.length() - 4);
        }

        return path;
    }

    private static void cloneRepository(final Path repoDir, @Nullable final Path gitPath, final URI gitRepoUri, final String gitBranch, final PrintStream out) throws IOException {
        out.println("Attempting to clone: " + gitRepoUri + " into: " + repoDir);

        try {
            final Process process = new ProcessBuilder(getGitBin(gitPath), "clone", "--single-branch", "--branch", gitBranch, gitRepoUri.toString())
                .directory(repoDir.getParent().toFile())
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

            if (process.waitFor(15, TimeUnit.MINUTES)) {
                final int exitValue = process.exitValue();
                if (exitValue != 0) {
                    throw new IOException("Executing `git clone` (into: " + repoDir + ") returned the exit code: " + exitValue);
                }
            } else {
                throw new IOException("Executing `git clone` (into: " + repoDir + ") did not complete within 15 minutes");
            }
        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new IOException(e.getMessage(), e);
        }
    }

    private static void fetchForRepository(final Path repoDir, @Nullable final Path gitPath, final PrintStream out) throws IOException {
        final URI remoteUri = getRemoteUri(repoDir, gitPath, "origin");
        out.println("Attempting to fetch updates for: " + repoDir + " from: " + remoteUri);

        try {
            final Process process = new ProcessBuilder(getGitBin(gitPath), "fetch")
                .directory(repoDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

            if (process.waitFor(15, TimeUnit.MINUTES)) {
                final int exitValue = process.exitValue();
                if (exitValue != 0) {
                    throw new IOException("Executing `git fetch` (in: " + repoDir + ") returned the exit code: " + exitValue);
                }
            } else {
                throw new IOException("Executing `git fetch` (in: " + repoDir + ") did not complete within 15 minutes");
            }
        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new IOException(e.getMessage(), e);
        }
    }

    private static URI getRemoteUri(final Path repoDir, @Nullable final Path gitPath, final String remoteName) throws IOException {
        try {
            final Process process = new ProcessBuilder(getGitBin(gitPath), "remote", "get-url", "--no-push", remoteName)
                .directory(repoDir.toFile())
                .start();

            if (process.waitFor(5, TimeUnit.SECONDS)) {
                final int exitValue = process.exitValue();
                if (exitValue != 0) {
                    throw new IOException("Executing `git remote` (in: " + repoDir + ") returned the exit code: " + exitValue);
                }

                final StringBuilder builder = new StringBuilder();
                int read = -1;
                final char[] buf = new char[128];
                try (final BufferedReader reader = process.inputReader(StandardCharsets.UTF_8)) {
                    while ((read = reader.read(buf)) != -1) {
                        builder.append(buf, 0, read);
                    }
                }

                final String strUri = builder.toString().trim();
                try {
                    return URI.create(strUri);
                } catch (final IllegalArgumentException e) {
                    throw new IOException("Unable to parse git remote as URI: " + strUri);
                }

            } else {
                throw new IOException("Executing `git remote` (in: " + repoDir + ") did not complete within 5 seconds");
            }

        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new IOException(e.getMessage(), e);
        }
    }
}
