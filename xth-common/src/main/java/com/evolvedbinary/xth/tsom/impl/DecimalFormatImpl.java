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
package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.DecimalFormat;
import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;

public final class DecimalFormatImpl implements DecimalFormat {
    private final QName name;
    @Nullable private final Character decimalSeparator;
    @Nullable private final Character groupingSeparator;
    @Nullable private final Character zeroDigit;
    @Nullable private final Character digit;
    @Nullable private final Character minusSign;
    @Nullable private final Character percent;
    @Nullable private final Character perMille;
    @Nullable private final Character patternSeparator;
    @Nullable private final Character exponentSeparator;
    @Nullable private final String infinity;
    @Nullable private final String nan;

    public DecimalFormatImpl(final QName name, @Nullable final Character decimalSeparator, @Nullable final Character groupingSeparator, @Nullable final Character zeroDigit, @Nullable final Character digit, @Nullable final Character minusSign, @Nullable final Character percent, @Nullable final Character perMille, @Nullable final Character patternSeparator, @Nullable final Character exponentSeparator, @Nullable final String infinity, @Nullable final String nan) {
        this.name = name;
        this.decimalSeparator = decimalSeparator;
        this.groupingSeparator = groupingSeparator;
        this.zeroDigit = zeroDigit;
        this.digit = digit;
        this.minusSign = minusSign;
        this.percent = percent;
        this.perMille = perMille;
        this.patternSeparator = patternSeparator;
        this.exponentSeparator = exponentSeparator;
        this.infinity = infinity;
        this.nan = nan;
    }

    @Override
    public QName getName() {
        return name;
    }

    @Override
    public @Nullable Character getDecimalSeparator() {
        return decimalSeparator;
    }

    @Override
    public @Nullable Character getGroupingSeparator() {
        return groupingSeparator;
    }

    @Override
    public @Nullable Character getZeroDigit() {
        return zeroDigit;
    }

    @Override
    public @Nullable Character getDigit() {
        return digit;
    }

    @Override
    public @Nullable Character getMinusSign() {
        return minusSign;
    }

    @Override
    public @Nullable Character getPercent() {
        return percent;
    }

    @Override
    public @Nullable Character getPerMille() {
        return perMille;
    }

    @Override
    public @Nullable Character getPatternSeparator() {
        return patternSeparator;
    }

    @Override
    public @Nullable Character getExponentSeparator() {
        return exponentSeparator;
    }

    @Override
    public @Nullable String getInfinity() {
        return infinity;
    }

    @Override
    public @Nullable String getNaN() {
        return nan;
    }
}
