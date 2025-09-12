package com.evolvedbinary.xth.tsom;

import org.jspecify.annotations.Nullable;

public interface Environment {

    /**
     * Get the name of the Environment.
     *
     * @return the name of the Environment.
     */
    @Nullable String getName();

    interface Builder {
        Environment build();
    }
}
