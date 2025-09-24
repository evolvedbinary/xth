package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.CollectionImpl;

import java.net.URI;
import java.util.List;

public sealed interface Collection permits CollectionImpl {
    URI getUri();
    List<Source> getSource();
    List<Resource> getResource();
    List<String> getQuery();
}
