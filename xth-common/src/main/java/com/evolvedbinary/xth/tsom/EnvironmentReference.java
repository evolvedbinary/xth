package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.EnvironmentReferenceImpl;

public sealed interface EnvironmentReference extends Environment permits EnvironmentReferenceImpl {
    sealed interface Builder extends Environment.Builder permits EnvironmentReferenceImpl.Builder {
    }
}
