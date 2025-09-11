package com.evolvedbinary.xth.scm;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class GitTest {

    @Test
    public void getRepoPathParts() {
        final String[] expected = new String[] { "w3c", "qt3tests" };

        assertArrayEquals(expected, Git.getRepoPathParts(URI.create("https://github.com/w3c/qt3tests.git/")));
        assertArrayEquals(expected, Git.getRepoPathParts(URI.create("https://github.com/w3c/qt3tests.git")));
        assertArrayEquals(expected, Git.getRepoPathParts(URI.create("https://github.com/w3c/qt3tests/")));
        assertArrayEquals(expected, Git.getRepoPathParts(URI.create("https://github.com/w3c/qt3tests")));
        assertThrows(IllegalArgumentException.class, () -> Git.getRepoPathParts(URI.create("https://github/")));
        assertThrows(IllegalArgumentException.class, () -> Git.getRepoPathParts(URI.create("https://github")));
    }

    @Test
    public void getRepoName() {
        final String expected = "qt3tests";

        assertEquals(expected, Git.getRepoName(URI.create("https://github.com/w3c/qt3tests.git/")));
        assertEquals(expected, Git.getRepoName(URI.create("https://github.com/w3c/qt3tests.git")));
        assertEquals(expected, Git.getRepoName(URI.create("https://github.com/w3c/qt3tests/")));
        assertEquals(expected, Git.getRepoName(URI.create("https://github.com/w3c/qt3tests")));
        assertThrows(IllegalArgumentException.class, () -> Git.getRepoName(URI.create("https://github/")));
        assertThrows(IllegalArgumentException.class, () -> Git.getRepoName(URI.create("https://github")));
    }
}
