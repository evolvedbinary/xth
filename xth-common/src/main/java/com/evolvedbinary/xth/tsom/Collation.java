package com.evolvedbinary.xth.tsom;

import java.net.URI;

public interface Collation {

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
