package com.evolvedbinary.xth.tsom;

public interface ContextItem {

    /**
     * Get the Select XPath expression to evaluate for the Context Item.
     *
     * @return a simple XPath expression which can be evaluated to set the Context Item.
     */
    String getSelect();
}
