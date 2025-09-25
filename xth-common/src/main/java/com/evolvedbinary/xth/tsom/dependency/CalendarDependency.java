package com.evolvedbinary.xth.tsom.dependency;

import com.evolvedbinary.xth.tsom.Dependency;
import com.evolvedbinary.xth.tsom.dependency.impl.CalendarDependencyImpl;

public sealed interface CalendarDependency extends Dependency<String> permits CalendarDependencyImpl {
}
