package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.SchemaAwareDependency;

public final class SchemaAwareDependencyImpl extends AbstractDependencyImpl<Boolean> implements SchemaAwareDependency {
    public SchemaAwareDependencyImpl(final boolean schemaAware, final boolean satisfied) {
        super(schemaAware, satisfied);
    }
}
