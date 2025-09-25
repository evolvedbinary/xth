package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.DirectoryAsCollectionUriDependencyImpl;

public sealed interface DirectoryAsCollectionUriDependency extends Dependency<Boolean> permits DirectoryAsCollectionUriDependencyImpl {
}
