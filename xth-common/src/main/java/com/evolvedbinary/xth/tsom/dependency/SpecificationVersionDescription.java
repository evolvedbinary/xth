package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.SpecificationVersion;

public interface SpecificationVersionDescription {
    SpecificationVersion getSpecificationVersion();
    boolean isNewerVersionAllowed();
}
