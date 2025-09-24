package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.VariableRoleImpl;

public sealed interface VariableRole extends Role permits VariableRoleImpl {
    String getName();
}
