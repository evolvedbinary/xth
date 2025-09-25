package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.DirectoryAsCollectionUriDependency;

public final class DirectoryAsCollectionUriDependencyImpl extends AbstractDependencyImpl<Boolean> implements DirectoryAsCollectionUriDependency {
    public DirectoryAsCollectionUriDependencyImpl(final boolean directoryAsCollectionUri, final boolean satisfied) {
        super(directoryAsCollectionUri, satisfied);
    }
}
