package com.evolvedbinary.xth.util;

import java.util.Collections;
import java.util.List;

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
}
