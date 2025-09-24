package com.evolvedbinary.xth.util;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public interface ListUtil {

    static <T> List<T> toImmutableList(final List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        } else if (list.size() == 1) {
            return Collections.singletonList(list.get(0));
        } else {
            return Collections.unmodifiableList(list);
        }
    }

    static <V> List<V> safeAdd(@Nullable List<V> list, final V value) {
        return safeAdd(list, value, ArrayList::new);
    }

    static <V> List<V> safeAdd(@Nullable List<V> list, final V value, final Supplier<List<V>> listConstructor) {
        if (list == null) {
            list = listConstructor.get();
        }
        list.add(value);
        return list;
    }
}
