package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.*;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;

public final class EnvironmentDefinitionImpl extends AbstractEnvironment implements EnvironmentDefinition {

    private final List<Schema> schemas;
    private final List<Source> sources;
    private final List<Resource> resources;
    private final List<Parameter> parameters;
    private final @Nullable ContextItem contextItem;
    private final List<DecimalFormat> decimalFormats;
    private final List<Namespace> namespaces;
    private final List<FunctionLibrary> functionLibraries;
    private final List<Collection> collections;
    private final @Nullable StaticBaseUri staticBaseUri;
    private final List<Collation> collations;

    private EnvironmentDefinitionImpl(final URI baseUri, @Nullable final String name, final List<Schema> schemas, final List<Source> sources,
            final List<Resource> resources, final List<Parameter> parameters, final @Nullable ContextItem contextItem,
            final List<DecimalFormat> decimalFormats, final List<Namespace> namespaces,
            final List<FunctionLibrary> functionLibraries, final List<Collection> collections,
            final @Nullable StaticBaseUri staticBaseUri, final List<Collation> collations) {
        super(baseUri, name);
        this.schemas = schemas;
        this.sources = sources;
        this.resources = resources;
        this.parameters = parameters;
        this.contextItem = contextItem;
        this.decimalFormats = decimalFormats;
        this.namespaces = namespaces;
        this.functionLibraries = functionLibraries;
        this.collections = collections;
        this.staticBaseUri = staticBaseUri;
        this.collations = collations;
    }

    @Override
    public List<Schema> getSchemas() {
        return schemas;
    }

    @Override
    public List<Source> getSources() {
        return sources;
    }

    @Override
    public List<Resource> getResources() {
        return resources;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public @Nullable ContextItem getContextItem() {
        return contextItem;
    }

    @Override
    public List<DecimalFormat> getDecimalFormats() {
        return decimalFormats;
    }

    @Override
    public List<Namespace> getNamespaces() {
        return namespaces;
    }

    @Override
    public List<FunctionLibrary> getFunctionLibraries() {
        return functionLibraries;
    }

    @Override
    public List<Collection> getCollections() {
        return collections;
    }

    @Override
    public @Nullable StaticBaseUri getStaticBaseUri() {
        return staticBaseUri;
    }

    @Override
    public List<Collation> getCollations() {
        return collations;
    }

    public static Builder builder(final URI baseUri, @Nullable final String name) {
        return new Builder(baseUri, name);
    }

    public static final class Builder extends AbstractEnvironment.Builder implements EnvironmentDefinition.Builder {
        protected List<Schema> schemas = null;
        protected List<Source> sources = null;
        protected List<Resource> resources = null;
        protected List<Parameter> parameters = null;
        protected @Nullable ContextItem contextItem = null;
        protected List<DecimalFormat> decimalFormats = null;
        protected List<Namespace> namespaces = null;
        protected List<FunctionLibrary> functionLibraries = null;
        protected List<Collection> collections = null;
        protected @Nullable StaticBaseUri staticBaseUri = null;
        protected List<Collation> collations = null;

        private Builder(final URI baseUri, @Nullable final String name) {
            super(baseUri, name);
        }

        @Override
        public Builder addSchema(final Schema schema) {
            if (schemas == null) {
                schemas = new ArrayList<>();
            }
            schemas.add(schema);
            return this;
        }

        @Override
        public Builder addSource(final Source source) {
            if (sources == null) {
                sources = new ArrayList<>();
            }
            sources.add(source);
            return this;
        }

        @Override
        public Builder addResource(final Resource resource) {
            if (resources == null) {
                resources = new ArrayList<>();
            }
            resources.add(resource);
            return this;
        }

        @Override
        public Builder addParameter(final Parameter parameter) {
            if (parameters == null) {
                parameters = new ArrayList<>();
            }
            parameters.add(parameter);
            return this;
        }

        @Override
        public Builder setContextItem(@Nullable final ContextItem contextItem) {
            this.contextItem = contextItem;
            return this;
        }

        @Override
        public Builder addDecimalFormat(final DecimalFormat decimalFormat) {
            if (decimalFormats == null) {
                decimalFormats = new ArrayList<>();
            }
            decimalFormats.add(decimalFormat);
            return this;
        }

        @Override
        public Builder addNamespace(final Namespace namespace) {
            if (namespaces == null) {
                namespaces = new ArrayList<>();
            }
            namespaces.add(namespace);
            return this;
        }

        @Override
        public Builder addFunctionLibrary(final FunctionLibrary functionLibrary) {
            if (functionLibraries == null) {
                functionLibraries = new ArrayList<>();
            }
            functionLibraries.add(functionLibrary);
            return this;
        }

        @Override
        public Builder addCollection(final Collection collection) {
            if (collections == null) {
                collections = new ArrayList<>();
            }
            collections.add(collection);
            return this;
        }

        @Override
        public Builder setStaticBaseUri(@Nullable final StaticBaseUri staticBaseUri) {
            this.staticBaseUri = staticBaseUri;
            return this;
        }

        @Override
        public Builder addCollation(final Collation collation) {
            if (collations == null) {
                collations = new ArrayList<>();
            }
            collations.add(collation);
            return this;
        }

        @Override
        public Environment build() {
            return new EnvironmentDefinitionImpl(
                baseUri,
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
                toImmutableList(collations)
            );
        }
    }
}
