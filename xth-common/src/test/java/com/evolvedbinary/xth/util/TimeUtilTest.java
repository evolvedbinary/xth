package com.evolvedbinary.xth.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilTest {

    @Test
    public void toHumaneString() {
        assertEquals("0 ms", TimeUtil.toHumaneString(-999));
        assertEquals("0 ms", TimeUtil.toHumaneString(-1));
        assertEquals("0 ms", TimeUtil.toHumaneString(0));
        assertEquals("123 ms", TimeUtil.toHumaneString(123));
        assertEquals("999 ms", TimeUtil.toHumaneString(999));

        assertEquals("1 second", TimeUtil.toHumaneString(1 * 1000));
        assertEquals("1.500 seconds", TimeUtil.toHumaneString(1500));
        assertEquals("1.750 seconds", TimeUtil.toHumaneString(1750));
        assertEquals("10 seconds", TimeUtil.toHumaneString(10 * 1000));
        assertEquals("59 seconds", TimeUtil.toHumaneString(59 * 1000));

        assertEquals("1 minute", TimeUtil.toHumaneString(1 * 60 * 1000));
        assertEquals("2:30 minutes", TimeUtil.toHumaneString((long)(2.5 * 60 * 1000)));
        assertEquals("2:45 minutes", TimeUtil.toHumaneString((long)(2.75 * 60 * 1000)));
        assertEquals("2:45.060 minutes", TimeUtil.toHumaneString((long)(2.751 * 60 * 1000)));
        assertEquals("10 minutes", TimeUtil.toHumaneString(10 * 60 * 1000));
        assertEquals("59 minutes", TimeUtil.toHumaneString(59 * 60 * 1000));

        assertEquals("1 hour", TimeUtil.toHumaneString(1 * 60 * 60 * 1000));
        assertEquals("1:30 hours", TimeUtil.toHumaneString((long)(1.5 * 60 * 60 * 1000)));
        assertEquals("1:45 hours", TimeUtil.toHumaneString((long)(1.75 * 60 * 60 * 1000)));
        assertEquals("1:45:19.800 hours", TimeUtil.toHumaneString((long)(1.7555 * 60 * 60 * 1000)));
        assertEquals("10 hours", TimeUtil.toHumaneString(10 * 60 * 60 * 1000));
        assertEquals("59 hours", TimeUtil.toHumaneString(59 * 60 * 60 * 1000));
        assertEquals("110 hours", TimeUtil.toHumaneString(110 * 60 * 60 * 1000));
    }
}
