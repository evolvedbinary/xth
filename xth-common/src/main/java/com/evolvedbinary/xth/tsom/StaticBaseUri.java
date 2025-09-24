package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.StaticBaseUriImpl;

import java.net.URI;

public sealed interface StaticBaseUri permits StaticBaseUriImpl {

    /**
     * Get the Static Base URI.
     *
     * @return the Static Base URI.
     */
    URI getUri();
}
