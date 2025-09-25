package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.FormatIntegerSequenceDependencyImpl;

public sealed interface FormatIntegerSequenceDependency extends Dependency<String> permits FormatIntegerSequenceDependencyImpl {
}
