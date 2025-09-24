package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.Modified;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;

public final class ModifiedImpl implements Modified {
    private final String by;
    private final XMLGregorianCalendar on;
    private final String change;

    public ModifiedImpl(final String by, final XMLGregorianCalendar on, final String change) {
        this.by = by;
        this.on = on;
        this.change = change;
    }

    @Override
    public String getBy() {
        return by;
    }

    @Override
    public XMLGregorianCalendar getOn() {
        return on;
    }

    @Override
    public String getChange() {
        return change;
    }
}
