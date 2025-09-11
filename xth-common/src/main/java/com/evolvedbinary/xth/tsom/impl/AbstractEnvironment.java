package com.evolvedbinary.xth.tsom.impl;

import com.evolvedbinary.xth.tsom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEnvironment implements Environment {

    private final List<Schema> schemas;
    private final List<Source> sources;
    private final List<Resource> resources;
    private final List<Parameter> parameters;
    private final Optional<ContextItem> contextItem;
    private final List<DecimalFormat> decimalFormats;
    private final List<Namespace> namespaces;
    private final List<FunctionLibrary> functionLibraries;
    private final List<Collection> collections;
    private final Optional<StaticBaseUri> staticBaseUri;
    private final Optional<Collation> collation;

    protected AbstractEnvironment(final List<Schema> schemas, final List<Source> sources,
                                  final List<Resource> resources, final List<Parameter> parameters, final Optional<ContextItem> contextItem,
                                  final List<DecimalFormat> decimalFormats, final List<Namespace> namespaces,
                                  final List<FunctionLibrary> functionLibraries, final List<Collection> collections,
                                  final Optional<StaticBaseUri> staticBaseUri, final Optional<Collation> collation) {
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
        this.collation = collation;
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
    public Optional<ContextItem> getContextItem() {
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
    public Optional<StaticBaseUri> getStaticBaseUri() {
        return staticBaseUri;
    }

    @Override
    public Optional<Collation> getCollation() {
        return collation;
    }

    protected abstract static class Builder implements Environment.Builder {
        protected List<Schema> schemas = null;
        protected List<Source> sources = null;
        protected List<Resource> resources = null;
        protected List<Parameter> parameters = null;
        protected Optional<ContextItem> contextItem = Optional.empty();
        protected List<DecimalFormat> decimalFormats = null;
        protected List<Namespace> namespaces = null;
        protected List<FunctionLibrary> functionLibraries = null;
        protected List<Collection> collections = null;
        protected Optional<StaticBaseUri> staticBaseUri = Optional.empty();
        protected Optional<Collation> collation = Optional.empty();

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
        public Builder setContextItem(final ContextItem contextItem) {
            this.contextItem = Optional.ofNullable(contextItem);
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
        public Builder setStaticBaseUri(final StaticBaseUri staticBaseUri) {
            this.staticBaseUri = Optional.ofNullable(staticBaseUri);
            return this;
        }

        @Override
        public Builder setCollation(final Collation collation) {
            this.collation = Optional.ofNullable(collation);
            return this;
        }
    }
}
