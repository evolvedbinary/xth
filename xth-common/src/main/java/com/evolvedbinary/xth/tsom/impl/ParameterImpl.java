package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Parameter;
import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;

public class ParameterImpl implements Parameter {
    private final QName name;
    @Nullable private final String select;
    @Nullable private final String as;
    @Nullable private final String source;
    private final boolean declared;

    public ParameterImpl(final QName name, @Nullable final String select, @Nullable final String as, @Nullable final String source, @Nullable final boolean declared) {
        this.name = name;
        this.select = select;
        this.as = as;
        this.source = source;
        this.declared = declared;
    }

    @Override
    public QName getName() {
        return name;
    }

    @Override
    public @Nullable String getSelect() {
        return select;
    }

    @Override
    public @Nullable String getAs() {
        return as;
    }

    @Override
    public @Nullable String getSource() {
        return source;
    }

    @Override
    public boolean isDeclared() {
        return declared;
    }
}
