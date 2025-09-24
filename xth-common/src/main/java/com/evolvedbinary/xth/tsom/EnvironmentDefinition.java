package com.evolvedbinary.xth.tsom;

import com.evolvedbinary.xth.tsom.impl.EnvironmentDefinitionImpl;
import org.jspecify.annotations.Nullable;

import java.util.List;

public sealed interface EnvironmentDefinition extends Environment permits EnvironmentDefinitionImpl {

    /**
     * Get Schemas.
     *
     * @return zero or more Schemas.
     */
    List<Schema> getSchemas();

    /**
     * Get Sources.
     *
     * @return zero or more Sources.
     */
    List<Source> getSources();

    /**
     * Get Resources.
     *
     * @return zero or more Resources.
     */
    List<Resource> getResources();

    /**
     * Get Parameters.
     *
     * @return zero or more Parameters.
     */
    List<Parameter> getParameters();

    /**
     * Get the Context Item.
     *
     * @return the Context Item.
     */
    @Nullable
    ContextItem getContextItem();

    /**
     * Get the Decimal Formats.
     *
     * @return zero or more Decimal Formats.
     */
    List<DecimalFormat> getDecimalFormats();

    /**
     * Get the Namespaces.
     *
     * @return zero or more Namespaces.
     */
    List<Namespace> getNamespaces();

    /**
     * Get the Function Libraries.
     *
     * @return zero or more Function Libraries.
     */
    List<FunctionLibrary> getFunctionLibraries();

    /**
     * Get the Collections.
     *
     * @return zero or more Collections.
     */
    List<Collection> getCollections();

    /**
     * Get the Static Base URI.
     *
     * @return the Static Base URI.
     */
    @Nullable StaticBaseUri getStaticBaseUri();

    /**
     * Get the Collations.
     *
     * @return the Collations.
     */
    List<Collation> getCollations();

    sealed interface Builder extends Environment.Builder permits EnvironmentDefinitionImpl.Builder {
        Builder addSchema(Schema schema);
        Builder addSource(Source source);
        Builder addResource(Resource resource);
        Builder addParameter(Parameter parameter);
        Builder setContextItem(ContextItem contextItem);
        Builder addDecimalFormat(DecimalFormat decimalFormat);
        Builder addNamespace(Namespace namespace);
        Builder addFunctionLibrary(FunctionLibrary functionLibrary);
        Builder addCollection(Collection collection);
        Builder setStaticBaseUri(StaticBaseUri staticBaseUri);
        Builder addCollation(Collation collation);
    }
}
