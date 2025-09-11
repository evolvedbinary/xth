package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;

public interface Parameter {
    QName getName();
    @Nullable String getSelect();
    @Nullable String getAs();
    @Nullable String getSource();
    boolean isDeclared();
}
