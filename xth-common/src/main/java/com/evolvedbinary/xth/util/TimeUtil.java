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
