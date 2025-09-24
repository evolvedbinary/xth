package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.ModifiedImpl;

import javax.xml.datatype.XMLGregorianCalendar;

public sealed interface Modified permits ModifiedImpl {
    String getBy();
    XMLGregorianCalendar getOn();
    String getChange();
}
