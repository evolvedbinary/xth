package com.evolvedbinary.xth.tsom;

import java.net.URI;
import java.util.List;

public interface Collection {
    URI getUri();
    List<Source> getSource();
    List<Resource> getResource();
    List<String> getQuery();
}
