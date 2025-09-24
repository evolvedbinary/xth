package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.ContextItem;

public final class ContextItemImpl implements ContextItem {
    private final String select;

    public ContextItemImpl(final String select) {
        this.select = select;
    }

    @Override
    public String getSelect() {
        return select;
    }
}
