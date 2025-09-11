package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NamedEnvironmentImpl extends AbstractEnvironment implements Environment, NamedEnvironment {

    private final String name;

    private NamedEnvironmentImpl(final String name, final List<Schema> schemas, final List<Source> sources,
                                 final List<Resource> resources, final List<Parameter> parameters, final Optional<ContextItem> contextItem,
                                 final List<DecimalFormat> decimalFormats, final List<Namespace> namespaces,
                                 final List<FunctionLibrary> functionLibraries, final List<Collection> collections,
                                 final Optional<StaticBaseUri> staticBaseUri, final Optional<Collation> collation) {
        super(schemas, sources, resources, parameters, contextItem, decimalFormats, namespaces, functionLibraries, collections, staticBaseUri, collation);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Builder builder(final String name) {
        return new Builder(name);
    }

    public static class Builder extends AbstractEnvironment.Builder implements Environment.Builder {
        private final String name;

        private Builder(final String name) {
            this.name = name;
        }

        @Override
        public Environment build() {
            return new NamedEnvironmentImpl(
                name,
                toImmutableList(schemas),
                toImmutableList(sources),
                toImmutableList(resources),
                toImmutableList(parameters),
                contextItem,
                toImmutableList(decimalFormats),
                toImmutableList(namespaces),
                toImmutableList(functionLibraries),
                toImmutableList(collections),
                staticBaseUri,
                collation
            );
        }
    }

    private static <T> List<T> toImmutableList(final List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        } else if (list.size() == 1) {
            return Collections.singletonList(list.get(0));
        } else {
            return Collections.unmodifiableList(list);
        }
    }
}
