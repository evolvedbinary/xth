package com.evolvedbinary.xth.tsom.assertion;

import com.evolvedbinary.xth.tsom.Assertion;

import java.util.List;

public interface SequenceOfAssertions extends Assertion {
    List<Assertion> getAssertions();
}
