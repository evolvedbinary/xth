package com.evolvedbinary.xth.tsom;

public enum Specification {
    XML("XML", "eXtensible Markup Language"),
    XSD("XSD", "XML Schema Definition Language"),
    XPATH("XPath", "XML Path Language"),
    XQUERY("XQuery", "XML Query Language"),
    XSLT("XSLT", "XSL Transformations");

    private final String shortName;
    private final String fullName;

    Specification(final String shortName, final String fullName) {
        this.shortName = shortName;
        this.fullName = fullName;
    }
}
