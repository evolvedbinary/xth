package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.dependency.SpecificationVersionDescription;

public class SpecificationVersionDescriptionImpl implements SpecificationVersionDescription {
    final SpecificationVersion specificationVersion;
    final boolean newerVersionAllowed;

    public SpecificationVersionDescriptionImpl(final SpecificationVersion specificationVersion, final boolean newerVersionAllowed) {
        this.specificationVersion = specificationVersion;
        this.newerVersionAllowed = newerVersionAllowed;
    }

    @Override
    public SpecificationVersion getSpecificationVersion() {
        return specificationVersion;
    }

    @Override
    public boolean isNewerVersionAllowed() {
        return newerVersionAllowed;
    }

    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof SpecificationVersionDescriptionImpl that)) {
            return false;
        }
        return newerVersionAllowed == that.newerVersionAllowed && specificationVersion == that.specificationVersion;
    }

    @Override
    public int hashCode() {
        int result = specificationVersion.hashCode();
        result = 31 * result + Boolean.hashCode(newerVersionAllowed);
        return result;
    }

    /**
     * Creates a Specification Version from its FOTS specification name, e.g. XQ30 or XP30+
     *
     * @param fotsName the FOTS name for the specification
     *
     * @return the specification version
     *
     * @throws IllegalArgumentException if there is no defined specification for the FOTS name.
     */
    public static SpecificationVersionDescription fromFotsName(String fotsName) throws IllegalArgumentException {
        final boolean newerVersionAllowed;
        if (fotsName.endsWith("+")) {
            fotsName = fotsName.substring(0, fotsName.length() - 1);
            newerVersionAllowed = true;
        } else {
            newerVersionAllowed = false;
        }
        final SpecificationVersion specification = SpecificationVersion.fromFotsName(fotsName);
        return new SpecificationVersionDescriptionImpl(specification, newerVersionAllowed);
    }
}
