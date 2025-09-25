package com.evolvedbinary.xth.tsom;

import static com.evolvedbinary.xth.tsom.Specification.*;

/**
 * Enumeration of relevant W3C Specifications.
 */
public enum SpecificationVersion {
    XPATH_4_0(XPATH, 4.0f, "XP40"),
    XPATH_3_1(XPATH, 3.1f, "XP31", XPATH_4_0),
    XPATH_3_0(XPATH, 3.0f, "XP30", XPATH_3_1, XPATH_4_0),
    XPATH_2_0(XPATH, 2.0f, "XP20", XPATH_3_0, XPATH_3_1, XPATH_4_0),
    XPATH_1_0(XPATH, 1.0f, "XP10", XPATH_2_0, XPATH_3_0, XPATH_3_1, XPATH_4_0),

    XQUERY_4_0(XQUERY, 4.0f, "XQ40"),
    XQUERY_3_1(XQUERY, 3.1f, "XQ31", XQUERY_4_0),
    XQUERY_3_0(XQUERY, 3.0f, "XQ30", XQUERY_3_1, XQUERY_4_0),
    XQUERY_1_0(XQUERY, 1.0f, "XQ10", XQUERY_3_0, XQUERY_3_1, XQUERY_4_0),

    XSLT_3_0(XSLT, 3.0f, "XT30"),
    XSLT_2_0(XSLT, 2.0f, "XT20", XSLT_3_0),
    XSLT_1_0(XSLT, 1.0f, "XT10", XSLT_2_0, XSLT_3_0);

    private final Specification specification;
    private final float version;
    private final String fotsName;
    private final SpecificationVersion[] newerVersions;

    SpecificationVersion(final Specification specification, final float version, final String fotsName, final SpecificationVersion... newerVersions) {
        this.specification = specification;
        this.version = version;
        this.fotsName = fotsName;
        this.newerVersions = newerVersions;
    }

    /**
     * Get a Specification from its FOTS specification name.
     *
     * @param fotsName the FOTS name for the specification, e.g. 'XP30' = XPath 3.0
     *
     * @return the specification
     *
     * @throws IllegalArgumentException if there is no defined specification for the FOTS name.
     */
    public static SpecificationVersion fromFotsName(final String fotsName) {
        for (final SpecificationVersion specification : SpecificationVersion.values()) {
            if (specification.fotsName.equals(fotsName)) {
                return specification;
            }
        }
        throw new IllegalArgumentException(String.format("There is no Specification for the FOTS specification name: %s", fotsName));
    }

    public Specification getSpecification() {
        return specification;
    }

    public String getVersion() {
        return String.format("%.1f", version);
    }

    public String getFotsName() {
        return fotsName;
    }

    public int compare(final SpecificationVersion other) {
        if (specification != other.specification) {
            return 1;
        }

        return Float.compare(version, other.version);
    }

    public SpecificationVersion[] getNewerVersions() {
        return newerVersions;
    }
}
