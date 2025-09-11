package com.evolvedbinary.xth.parser.fots31;

import com.evolvedbinary.xth.parser.api.ParserException;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.tsom.ContextItemRole;
import com.evolvedbinary.xth.tsom.ValidationMode;
import com.evolvedbinary.xth.tsom.XsdVersion;
import com.evolvedbinary.xth.tsom.impl.*;
import jakarta.validation.constraints.Null;
import org.jspecify.annotations.Nullable;
import org.w3._2010._09.qt_fots_catalog.*;
import org.w3._2010._09.qt_fots_catalog.Catalog.TestSet;

import javax.swing.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.evolvedbinary.xth.parser.fots31.JAXBUtil.unmarshal;

public class FOTS31Parser implements TestSuiteParser {

    private final Path testSuiteDirectory;

    FOTS31Parser(final Path testSuiteDirectory) {
        this.testSuiteDirectory = testSuiteDirectory;
    }

    @Override
    public String getParserName() {
        return FOTS31Constants.CATALOG_TEST_SUITE + " " + FOTS31Constants.CATALOG_VERSION;
    }

    @Override
    public void parse() throws IOException, ParserException {
        final Path catalogSchemaFile = testSuiteDirectory.resolve(FOTS31Constants.CATALOG_SCHEMA_FILE_NAME);
        final Path catalogFile = testSuiteDirectory.resolve(FOTS31Constants.CATALOG_FILE_NAME);

        final Catalog catalog = unmarshal(catalogSchemaFile, Catalog.class, catalogFile);

        processEnvironments(catalog.getEnvironment());
        processTestSets(catalog.getTestSet());
    }

    private List<com.evolvedbinary.xth.tsom.Environment> processEnvironments(final List<Environment> environments) throws ParserException {
        final List<com.evolvedbinary.xth.tsom.Environment> results = new ArrayList<>(environments.size());
        for (final Environment environment : environments) {
            results.add(processEnvironment(environment));
        }
        return results;
    }

    private com.evolvedbinary.xth.tsom.Environment processEnvironment(final Environment environment) throws ParserException {
        final String name = environment.getName();
        if (name == null) {
            throw new ParserException("Environments specified in the catalog must have a name");
        }

        final com.evolvedbinary.xth.tsom.Environment.Builder builder = NamedEnvironmentImpl.builder(name);
        for (final Object obj : environment.getSchemaOrSourceOrResource()) {
            switch (obj) {
                case SchemaType schema -> builder.addSchema(toTsom(schema));
                case SourceType source -> builder.addSource(toTsom(source));
                case ResourceType resource -> builder.addResource(toTsom(resource));
                case Param parameter -> builder.addParameter(toTsom(parameter));
                case ContextItem contextItem -> builder.setContextItem(toTsom(contextItem));
                case DecimalFormat decimalFormal -> builder.addDecimalFormat(toTsom(decimalFormal));
                case Namespace namespace -> builder.addNamespace(toTsom(namespace));
                case FunctionLibrary functionLibrary -> builder.addFunctionLibrary(toTsom(functionLibrary));
                case Collection collection -> builder.addCollection(toTsom(collection));
                case StaticBaseUri staticBaseUri -> builder.setStaticBaseUri(toTsom(staticBaseUri));
                case Collation collation -> builder.setCollation(toTsom(collation));
                default ->
                    throw new ParserException("Unable environment object: " + obj.getClass().getName());
            }
        }

        return builder.build();
    }

    private void processTestSets(final List<TestSet> testSets) {
        for (final TestSet testSet: testSets) {

        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Schema toTsom(@Nullable final SchemaType schema) throws ParserException {
        if (schema == null) {
            return null;
        }

        try {
            return new com.evolvedbinary.xth.tsom.impl.SchemaImpl(
                schema.getId(),
                schema.getDescription().getValue(),
                toTsom(schema.getCreated()),
                toTsom(schema.getModified()),
                new URI(schema.getUri()),
                new URI(schema.getFile()),
                toXsdVersion(schema.getXsdVersion()),
                toTsom(schema.getRole())
            );
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Source toTsom(@Nullable final SourceType source) throws ParserException {
        if (source == null) {
            return null;
        }

        try {
            return new com.evolvedbinary.xth.tsom.impl.SourceImpl(
                source.getId(),
                source.getDescription().getValue(),
                toTsom(source.getCreated()),
                toTsom(source.getModified()),
                toTsom(source.getRole()),
                new URI(source.getFile()),
                new URI(source.getUri()),
                toTsom(source.getValidation())
            );
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Resource toTsom(@Nullable final ResourceType resource) throws ParserException {
        if (resource == null) {
            return null;
        }

        try {
            return new com.evolvedbinary.xth.tsom.impl.ResourceImpl(
                resource.getId(),
                resource.getDescription().getValue(),
                toTsom(resource.getCreated()),
                toTsom(resource.getModified()),
                new URI(resource.getFile()),
                new URI(resource.getUri()),
                resource.getMediaType(),
                resource.getEncoding()
            );
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Parameter toTsom(@Nullable final Param parameter) {
        if (parameter == null) {
            return null;
        }
        return new ParameterImpl(
            parameter.getName(),
            parameter.getSelect(),
            parameter.getAs(),
            parameter.getSource(),
            parameter.isDeclared()
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable ContextItem toTsom(@Nullable final ContextItem contextItem) {
        if (contextItem == null) {
            return null;
        }
        return new ContextItemImpl(
            contextItem.getSelect()
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable DecimalFormat toTsom(@Nullable final DecimalFormat decimalFormat) {
        if (decimalFormat == null) {
            return null;
        }
        return new DecimalFormatImpl(
            decimalFormat.getName(),
            firstCharacter(decimalFormat.getDecimalSeparator()),
            firstCharacter(decimalFormat.getGroupingSeparator()),
            firstCharacter(decimalFormat.getZeroDigit()),
            firstCharacter(decimalFormat.getDigit()),
            firstCharacter(decimalFormat.getMinusSign()),
            firstCharacter(decimalFormat.getPercent()),
            firstCharacter(decimalFormat.getPerMille()),
            firstCharacter(decimalFormat.getPatternSeparator()),
            firstCharacter(decimalFormat.getExponentSeparator()),
            decimalFormat.getInfinity(),
            decimalFormat.getNaN()
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Namespace toTsom(@Nullable final Namespace namespace) {
        if (namespace == null) {
            return null;
        }
        return new NamespaceImpl(
            namespace.getPrefix(),
            namespace.getUri()
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable FunctionLibrary toTsom(@Nullable final FunctionLibrary functionLibrary) {
        if (functionLibrary == null) {
            return null;
        }
        return new FunctionLibraryImpl(
            functionLibrary.getName(),
            functionLibrary.getXsltLocation(),
            functionLibrary.getXqueryLocation()
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Collection toTsom(@Nullable final Collection collection) throws ParserException {
        if (collection == null) {
            return null;
        }

        final List<com.evolvedbinary.xth.tsom.Source> sources = new ArrayList<>(collection.getSource().size());
        for (final SourceType source : collection.getSource()) {
                sources.add(toTsom(source));
        }

        final List<com.evolvedbinary.xth.tsom.Resource> resources = new ArrayList<>(collection.getResource().size());
        for (final ResourceType resource : collection.getResource()) {
            resources.add(toTsom(resource));
        }

        final List<String> queries = new ArrayList<>(collection.getQuery().size());
        for (final Object query : collection.getQuery()) {
            queries.add(query.toString());
        }

        try {
            return new CollectionImpl(
                new URI(collection.getUri()),
                sources,
                resources,
                queries
            );
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable StaticBaseUri toTsom(@Nullable final StaticBaseUri staticBaseUri) throws ParserException {
        if (staticBaseUri == null) {
            return null;
        }
        try {
            return new StaticBaseUriImpl(new URI(staticBaseUri.getUri()));
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Collation toTsom(final Collation collation) throws ParserException {
        if (collation == null) {
            return null;
        }
        try {
            return new CollationImpl(
                new URI(collation.getUri()),
                collation.isDefault()
            );
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    private static XsdVersion toXsdVersion(final BigDecimal number) throws ParserException {
        if (number.floatValue() == 1.0f) {
            return XsdVersion.XSD_1_0;

        } else if (number.floatValue() == 1.1f) {
            return XsdVersion.XSD_1_1;

        } else {
            throw new ParserException("Unknown schema version: " + number);
        }
    }

    private static ValidationMode toTsom(final ValidationEnumType validation) {
        return switch (validation) {
            case ValidationEnumType.STRICT -> ValidationMode.STRICT;
            case ValidationEnumType.LAX -> ValidationMode.LAX;
            case ValidationEnumType.SKIP -> ValidationMode.SKIP;
        };
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Created toTsom(@Nullable final Created created) {
        if (created == null) {
            return null;
        }
        return new CreatedImpl(created.getBy(), created.getOn());
    }

    private static @Nullable List<com.evolvedbinary.xth.tsom.Modified> toTsom(final List<Modified> modifieds) {
        return modifieds.stream()
            .map(modified -> new ModifiedImpl(modified.getBy(), modified.getOn(), modified.getChange()))
            .collect(Collectors.toUnmodifiableList());
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Role toTsom(@Nullable final String role) {
        if (role == null) {
            return null;
        } else if(".".equals(role)) {
            return ContextItemRole.INSTANCE;
        } else {
            return new VariableRoleImpl(role);
        }
    }

    private static @Nullable Character firstCharacter(@Nullable final String str) {
        if (str == null) {
            return null;
        }
        return str.charAt(0);
    }
}
