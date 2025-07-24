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
