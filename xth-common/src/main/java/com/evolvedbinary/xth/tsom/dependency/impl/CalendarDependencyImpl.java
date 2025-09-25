package com.evolvedbinary.xth.tsom.dependency.impl;

import com.evolvedbinary.xth.tsom.dependency.CalendarDependency;

public final class CalendarDependencyImpl extends AbstractDependencyImpl<String> implements CalendarDependency {
    public CalendarDependencyImpl(final String calendarCode, final boolean satisfied) {
        super(calendarCode, satisfied);
    }
}
