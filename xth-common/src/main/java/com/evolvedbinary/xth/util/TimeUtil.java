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
package com.evolvedbinary.xth.util;

public interface TimeUtil {

    long MILLIS_IN_HOUR = 60 * 60 * 1000;
    long MILLIS_IN_MINUTE = 60 * 1000;
    long MILLIS_IN_SECOND = 1000;

    static String toHumaneString(long milliseconds) {

        if (milliseconds <= 0) {
            return "0 ms";
        }

        final long hours = milliseconds / MILLIS_IN_HOUR;
        milliseconds -= hours * MILLIS_IN_HOUR;
        final long minutes = milliseconds / MILLIS_IN_MINUTE;
        milliseconds -= minutes * MILLIS_IN_MINUTE;
        final long seconds = milliseconds / MILLIS_IN_SECOND;
        milliseconds -= seconds * MILLIS_IN_SECOND;

        String suffix = null;
        final StringBuilder builder = new StringBuilder();

        if (hours > 0) {
            if (hours == 1) {
                builder.append(hours);
            } else {
                builder.append(String.format("%02d", hours));
            }
            if (hours > 1 || minutes > 0 || seconds > 0 || milliseconds > 0) {
                suffix = "hours";
            } else {
                suffix = "hour";
            }
        }

        if (minutes > 0) {
            if (!builder.isEmpty()) {
                builder.append(':');
            }
            if (minutes == 1 || hours == 0) {
                builder.append(minutes);
            } else {
                builder.append(String.format("%02d", minutes));
            }
            if (suffix == null) {
                if (minutes > 1 || seconds > 0 || milliseconds > 0) {
                    suffix = "minutes";
                } else {
                    suffix = "minute";
                }
            }
        }

        if (seconds > 0) {
            if (!builder.isEmpty()) {
                builder.append(':');
            }
            if (seconds == 1 || (hours == 0 && minutes == 0)) {
                builder.append(seconds);
            } else {
                builder.append(String.format("%02d", seconds));
            }
            if (suffix == null) {
                if (seconds > 1 || milliseconds > 0) {
                    suffix = "seconds";
                } else {
                    suffix = "second";
                }
            }
        }

        if (milliseconds > 0) {
            if (!builder.isEmpty()) {
                builder.append('.');
            }
            if (hours == 0 && minutes == 0 && seconds == 0) {
                builder.append(milliseconds);
            } else {
                builder.append(String.format("%03d", milliseconds));
            }
            if (suffix == null) {
                suffix = "ms";
            }
        }

        return builder.append(' ').append(suffix).toString();
    }
}
