package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.CreatedImpl;

import javax.xml.datatype.XMLGregorianCalendar;

public sealed interface Created permits CreatedImpl {
    String getBy();
    XMLGregorianCalendar getOn();
}
