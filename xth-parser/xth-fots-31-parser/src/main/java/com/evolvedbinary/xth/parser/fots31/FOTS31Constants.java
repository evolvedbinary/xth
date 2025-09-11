package com.evolvedbinary.xth.parser.fots31;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

public class FOTS31Constants {

    static final String CATALOG_SCHEMA_FILE_NAME = "catalog.xsd";
    static final String CATALOG_FILE_NAME = "catalog.xml";
    static final String CATALOG_TEST_SUITE = "FOTS";
    static final String CATALOG_VERSION = "3.1";

    static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
}
