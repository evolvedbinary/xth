package com.evolvedbinary.xth.util;

import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public interface SetUtil {

    static <V> Set<V> toImmutableSet(final Set<V> set) {
        if (set == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(set);
        }
    }

    static <V> Set<V> safeAdd(@Nullable Set<V> set, final V value) {
        return safeAdd(set, value, HashSet::new);
    }

    static <V> Set<V> safeAdd(@Nullable Set<V> set, final V value, final Supplier<Set<V>> setConstructor) {
        if (set == null) {
            set = setConstructor.get();
        }
        set.add(value);
        return set;
    }
}
