package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.VariableRole;

public final class VariableRoleImpl implements VariableRole {
    private final String name;

    public VariableRoleImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
