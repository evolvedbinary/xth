package com.evolvedbinary.xth.configuration;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class XthProperties {

    private static @Nullable XthProperties instance;
    private static final StampedLock lock = new StampedLock();

    private final String productVersion;
    private final String productDescription;
    private final String productCopyright;
    private final URI defaultQtGitRepoUri;
    private final String defaultQtGitBranch;
    private final Path defaultCacheDirectory;
    private final Path defaultOutputDirectory;

    public XthProperties(final String productVersion, final String productDescription, final String productCopyright,
                         final URI defaultQtGitRepoUri, final String defaultQtGitBranch, final String defaultCacheDirectoryName,
                         final String defaultOutputDirectoryName) {
        this.productVersion = productVersion;
        this.productDescription = productDescription;
        this.productCopyright = productCopyright;
        this.defaultQtGitRepoUri = defaultQtGitRepoUri;
        this.defaultQtGitBranch = defaultQtGitBranch;
        this.defaultCacheDirectory = OSUtil.getOsCacheDir().resolve(defaultCacheDirectoryName).normalize();
        this.defaultOutputDirectory = OSUtil.getAppDir().resolve(defaultOutputDirectoryName).normalize();
    }

    public String getProductVersion() {
        return productVersion;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductCopyright() {
        return productCopyright;
    }

    public URI getDefaultQtGitRepoUri() {
        return defaultQtGitRepoUri;
    }

    public String getDefaultQtGitBranch() {
        return defaultQtGitBranch;
    }

    public Path getDefaultCacheDirectory() {
        return defaultCacheDirectory;
    }

    public Path getDefaultOutputDirectory() {
        return defaultOutputDirectory;
    }

    public static XthProperties getInstance() throws IOException {

        // try optimistic read
        final long optReadStamp = lock.tryOptimisticRead();
        if (instance != null) {
            if (lock.validate(optReadStamp)) {
                return instance;
            }
        }

        // otherwise... pessimistic read
        final long readStamp;
        try {
            readStamp = lock.tryReadLock(2, TimeUnit.SECONDS);
            if (readStamp == 0) {
                throw new IOException("Unable to acquire read lock");
            }
        } catch (final InterruptedException e) {
            // restore thread's interrupted flag
            Thread.currentThread().interrupt();
            throw new IOException(e.getMessage(), e);
        }

        if (instance != null) {
            // unlock read
            lock.unlockRead(readStamp);
            return instance;
        }

        // try to upgrade read lock to write lock
        final long writeStamp = lock.tryConvertToWriteLock(readStamp);
        if (writeStamp == 0) {
            lock.unlockRead(readStamp);
            throw new IOException("Unable to acquire write lock");
        }

        try {
            if (instance == null) {
                final Properties properties = loadProperties();
                final String productVersion = properties.getProperty("xth.product.version", "unknown");
                final String productDescription = properties.getProperty("xth.product.description");
                final String productCopyright = properties.getProperty("xth.product.copyright");
                final URI defaultQtGitRepoUri = URI.create(properties.getProperty("default.qt.git.repo.uri"));
                final String defaultQtGitBranch = properties.getProperty("default.qt.git.branch", "main");
                final String defaultCacheDirectoryName = properties.getProperty("default.cache.directory-name");
                final String defaultOutputDirectoryName = properties.getProperty("default.output.directory-name");

                instance = new XthProperties(productVersion, productDescription, productCopyright, defaultQtGitRepoUri, defaultQtGitBranch, defaultCacheDirectoryName, defaultOutputDirectoryName);
            }

            return instance;

        } finally {
            lock.unlockWrite(writeStamp);
        }
    }

    private static Properties loadProperties() throws IOException {
        final Properties properties = new Properties();
        try (final InputStream is = XthProperties.class.getResourceAsStream("xth.properties")) {
            properties.load(is);
        }
        return properties;
    }
}
