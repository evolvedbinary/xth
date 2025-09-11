package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;

public interface DecimalFormat {
    QName getName();
    @Nullable Character getDecimalSeparator();
    @Nullable Character getGroupingSeparator();
    @Nullable Character getZeroDigit();
    @Nullable Character getDigit();
    @Nullable Character getMinusSign();
    @Nullable Character getPercent();
    @Nullable Character getPerMille();
    @Nullable Character getPatternSeparator();
    @Nullable Character getExponentSeparator();
    @Nullable String getInfinity();
    @Nullable String getNaN();
}
