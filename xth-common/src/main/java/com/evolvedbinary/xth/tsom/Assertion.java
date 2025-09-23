package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.assertion.*;

public sealed interface Assertion permits Assert, AssertCount, AssertDeepEqual, AssertEmpty, AssertEqual, AssertError, AssertFalse, AssertNot, AssertPermutation, AssertSerializationError, AssertSerializationMatches, AssertStringValue, AssertTrue, AssertType, AssertXml, SequenceOfAssertions {
}
