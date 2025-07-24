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
package com.evolvedbinary.xth.util;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface MapUtil {

    static <K, V> Map<K, V> toImmutableMap(final Map<K, V> map) {
        if (map == null) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(map);
        }
    }

    static <K, V> Map<K, List<V>> safePutList(@Nullable Map<K, List<V>> map, final K key, final V value) {
        return safePutList(map, key, value, HashMap::new, ArrayList::new);
    }

    static <K, V> Map<K, List<V>> safePutList(@Nullable Map<K, List<V>> map, final K key, final V value, final Supplier<Map<K, List<V>>> mapConstructor, final Supplier<List<V>> listConstructor) {
        if (map == null) {
            map = mapConstructor.get();
        }
        map.compute(key, (k, vs) -> {
            if (vs == null) {
                vs = listConstructor.get();
            }
            vs.add(value);
            return vs;
        });
        return map;
    }

    static <K1, K2, V> Map<K1, Map<K2, V>> safePutMap(@Nullable Map<K1, Map<K2, V>> map, final K1 key1, final K2 key2, final V value) {
        return safePutMap(map, key1, key2, value, HashMap::new, HashMap::new);
    }

    static <K1, K2, V> Map<K1, Map<K2, V>> safePutMap(@Nullable Map<K1, Map<K2, V>> map, final K1 key1, final K2 key2, final V value, final Supplier<Map<K1, Map<K2, V>>> mapConstructor, final Supplier<Map<K2, V>> innerMapConstructor) {
        if (map == null) {
            map = mapConstructor.get();
        }
        map.compute(key1, (k, vs) -> {
            if (vs == null) {
                vs = innerMapConstructor.get();
            }
            vs.put(key2, value);
            return vs;
        });
        return map;
    }

    static <K, V> Map<K, V> safePut(@Nullable Map<K, V> map, final K key, final V value) {
        return safePut(map, key, value, HashMap::new);
    }

    static <K, V> Map<K, V> safePut(@Nullable Map<K, V> map, final K key, final V value, final Supplier<Map<K, V>> mapConstructor) {
        if (map == null) {
            map = mapConstructor.get();
        }
        map.put(key, value);
        return map;
    }
}
