/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
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
    public boolean isCompatibleWith(final SpecificationVersionDescription otherSpecificationVersionDescription) {
       if (this == otherSpecificationVersionDescription || this.equals(otherSpecificationVersionDescription)) {
           return true;
       }

       final SpecificationVersion otherSpecificationVersion = otherSpecificationVersionDescription.getSpecificationVersion();
       if (specificationVersion == otherSpecificationVersion) {
           return true;
       }

       if (isNewerVersionAllowed()) {
           for (final SpecificationVersion allowedNewerSpecificationVersion : specificationVersion.getNewerVersions()) {
               if (otherSpecificationVersion == allowedNewerSpecificationVersion) {
                   return true;
               }
           }
       }

       return false;
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
