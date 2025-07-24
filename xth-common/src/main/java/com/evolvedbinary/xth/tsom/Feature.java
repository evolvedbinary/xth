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
package com.evolvedbinary.xth.tsom;

public enum Feature {
    ARBITRARY_PRECISION_DECIMAL("arbitraryPrecisionDecimal"),
    ADVANCED_UCA_FALLBACK("advanced-uca-fallback"),
    COLLECTION_STABILITY("collection-stability"),
    DIRECTORY_AS_COLLECTION_URI("directory-as-collection-uri"),
    FN_FORMAT_INTEGER_CLDR("fn-format-integer-CLDR"),
    FN_LOAD_XQUERY_MODULE("fn-load-xquery-module"),
    FN_TRANSFORM_XSLT("fn-transform-XSLT"),
    FN_TRANSFORM_XSLT_3_0("fn-transform-XSLT30"),
    HIGHER_ORDER_FUNCTIONS("higherOrderFunctions"),
    INFOSET_DTD("infoset-dtd"),
    MODULE_IMPORT("moduleImport"),
    NAMESPACE_AXIS("namespace-axis"),
    NON_EMPTY_SEQUENCE_COLLECTION("non_empty_sequence_collection"),
    NON_UNICODE_CODEPOINT_COLLATION("non_unicode_codepoint_collation"),
    OLSON_TIMEZONE("olson-timezone"),
    REMOTE_HTTP("remote_http"),
    SCHEMA_IMPORT("schemaImport"),
    SCHEMA_LOCATION_HINT("schema-location-hint"),
    SCHEMA_VALIDATION("schemaValidation"),
    SERIALIZATION("serialization"),
    SIMPLE_UCA_FALLBACK("simple-uca-fallback"),
    STATIC_TYPING("staticTyping"),
    TYPED_DATA("typedData"),
    XPATH_1_0_COMPATIBILITY("xpath-1.0-compatibility");


    private final String fotsName;

    Feature(final String fotsName) {
        this.fotsName = fotsName;
    }

    /**
     * Get a Feature from its FOTS feature name.
     *
     * @param fotsName the FOTS name for the feature, e.g. 'advanced-uca-fallback'
     *
     * @return the feature
     *
     * @throws IllegalArgumentException if there is no defined feature for the FOTS name.
     */
    public static Feature fromFotsName(final String fotsName) {
        for (final Feature feature : Feature.values()) {
            if (feature.fotsName.equals(fotsName)) {
                return feature;
            }
        }
        throw new IllegalArgumentException(String.format("There is no Feature for the FOTS feature name: %s", fotsName));
    }

    public String getFotsName() {
        return fotsName;
    }
}
