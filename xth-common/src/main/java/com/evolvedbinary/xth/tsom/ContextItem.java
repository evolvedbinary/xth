package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.ContextItemImpl;

public sealed interface ContextItem permits ContextItemImpl {

    /**
     * Get the Select XPath expression to evaluate for the Context Item.
     *
     * @return a simple XPath expression which can be evaluated to set the Context Item.
     */
    String getSelect();
}
