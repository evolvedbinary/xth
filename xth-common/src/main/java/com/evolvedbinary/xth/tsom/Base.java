package com.evolvedbinary.xth.tsom;

public interface Base {

    /**
     * Get the xml:id.
     *
     * @return the xml:id
     */
    String getXmlId();

    interface Builder {
        void setXmlId(String xmlId);
    }
}
