package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Created;

import javax.xml.datatype.XMLGregorianCalendar;

public final class CreatedImpl implements Created {
    private final String by;
    private final XMLGregorianCalendar on;

    public CreatedImpl(final String by, final XMLGregorianCalendar on) {
        this.by = by;
        this.on = on;
    }

    @Override
    public String getBy() {
        return by;
    }

    @Override
    public XMLGregorianCalendar getOn() {
        return on;
    }
}
