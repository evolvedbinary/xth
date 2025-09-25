package com.evolvedbinary.xth.util;

import java.util.Objects;

public interface BooleanUtil {

    /**
     * Parses a string as a boolean,
     * truthy values are 'true' or 'yes'.
     *
     * @param str the string to parse.
     *
     * @return the boolean value.
     */
    static boolean parseToBoolean(String str) {
        Objects.requireNonNull(str, "Cannot parse null string to boolean");
        str = str.toLowerCase();
        return str.equals("true") || str.equals("yes");
    }
}
