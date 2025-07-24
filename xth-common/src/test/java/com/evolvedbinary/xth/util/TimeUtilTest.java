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
