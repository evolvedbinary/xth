package com.evolvedbinary.xth.cli;

import static com.evolvedbinary.xth.cli.ArgumentDefinition.*;

public enum ArgumentDefinitionGroup {

    GIT(
        GIT_PATH,
        GIT_REPO_URI,
        GIT_BRANCH
    ),
    IO(
        CACHE_DIR,
        OUTPUT_DIR
    );

    public final ArgumentDefinition[] members;

    ArgumentDefinitionGroup(final ArgumentDefinition... members) {
        this.members = members;
    }
}
