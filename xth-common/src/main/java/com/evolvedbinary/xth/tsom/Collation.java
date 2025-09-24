package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.CollationImpl;

import java.net.URI;

public sealed interface Collation permits CollationImpl {

    /**
     * Get the URI of the Collation.
     *
     * @return the URI of the Collation.
     */
    URI getUri();

    /**
     * Returns true if this collation should be used as the default collation in the static context.
     *
     * @return true if this collation should be used as the default collation in the static context, false otherwise.
     */
    boolean isDefault();
}
