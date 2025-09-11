package com.evolvedbinary.xth.tsom;

import javax.xml.datatype.XMLGregorianCalendar;

public interface Modified {
    String getBy();
    XMLGregorianCalendar getOn();
    String getChange();
}
