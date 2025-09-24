package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.ParameterImpl;
import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;

public sealed interface Parameter permits ParameterImpl {
    QName getName();
    @Nullable String getSelect();
    @Nullable String getAs();
    @Nullable String getSource();
    boolean isDeclared();
}
