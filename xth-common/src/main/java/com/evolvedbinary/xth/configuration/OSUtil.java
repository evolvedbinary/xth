package com.evolvedbinary.xth.configuration;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OSUtil {

    public static final String OS_NAME = System.getProperty("os.name");
    public static final Path USER_HOME = Paths.get(System.getProperty("user.home"));

    public static final boolean IS_WINDOWS = OS_NAME.toLowerCase().startsWith("windows");

    public static final boolean IS_LINUX =  OS_NAME.toLowerCase().startsWith("linux");

    public static final boolean IS_MAC_OSX = OS_NAME.toLowerCase().startsWith("mac os x");

    public static Path getOsCacheDir() {
        if (IS_MAC_OSX) {
            return USER_HOME.resolve("Library").resolve("Caches");

        } else if (IS_WINDOWS) {
            return USER_HOME.resolve("AppData").resolve("Local");

        } else {
            return USER_HOME.resolve(".cache");
        }
    }

    public static Path getAppDir() {
        final URL location = OSUtil.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            return Paths.get(location.toURI()).getParent();
        } catch (final URISyntaxException e) {
            return Paths.get(".");
        }
    }
}
