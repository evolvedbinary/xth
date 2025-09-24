package com.evolvedbinary.xth.parser.fots31;

import com.evolvedbinary.xth.parser.api.AbstractTestSuiteParser;
import com.evolvedbinary.xth.parser.api.ParserException;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.tsom.*;
import com.evolvedbinary.xth.tsom.assertion.impl.*;
import com.evolvedbinary.xth.tsom.impl.*;
import jakarta.xml.bind.JAXBElement;
import org.jspecify.annotations.Nullable;
import org.w3._2010._09.qt_fots_catalog.*;
import org.w3._2010._09.qt_fots_catalog.Collation;
import org.w3._2010._09.qt_fots_catalog.Collection;
import org.w3._2010._09.qt_fots_catalog.ContextItem;
import org.w3._2010._09.qt_fots_catalog.Created;
import org.w3._2010._09.qt_fots_catalog.DecimalFormat;
import org.w3._2010._09.qt_fots_catalog.Dependency;
import org.w3._2010._09.qt_fots_catalog.Environment;
import org.w3._2010._09.qt_fots_catalog.Error;
import org.w3._2010._09.qt_fots_catalog.FunctionLibrary;
import org.w3._2010._09.qt_fots_catalog.Link;
import org.w3._2010._09.qt_fots_catalog.Modified;
import org.w3._2010._09.qt_fots_catalog.Module;
import org.w3._2010._09.qt_fots_catalog.Namespace;
import org.w3._2010._09.qt_fots_catalog.StaticBaseUri;
import org.w3._2010._09.qt_fots_catalog.Test;
import org.w3._2010._09.qt_fots_catalog.TestCase;
import org.w3._2010._09.qt_fots_catalog.TestSet;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.evolvedbinary.xth.parser.fots31.JAXBUtil.unmarshal;
import static com.evolvedbinary.xth.util.IOUtil.path;

public class FOTS31Parser extends AbstractTestSuiteParser implements TestSuiteParser {

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
        final UUID parseId = generateUniqueId();
        final Path xmlSchemaFile = testSuiteDirectory.resolve(FOTS31Constants.XML_SCHEMA_FILE_NAME);
        final Path catalogSchemaFile = testSuiteDirectory.resolve(FOTS31Constants.CATALOG_SCHEMA_FILE_NAME);
        final Path catalogFile = testSuiteDirectory.resolve(FOTS31Constants.CATALOG_FILE_NAME);

        // Start parsing
        emitEvent(listener -> listener.startParseCatalog(parseId, catalogFile));

        final Catalog catalog = unmarshal(new Path[] { xmlSchemaFile, catalogSchemaFile}, Catalog.class, catalogFile);

        // Process Catalog Environment(s)
        emitEvent(listener -> listener.startParseCatalogEnvironments(parseId));
        processGlobalEnvironments(parseId, catalogFile, catalog.getEnvironment());
        emitEvent(listener -> listener.endParseCatalogEnvironments(parseId));

        // Process Catalog TestSet(s)
        emitEvent(listener -> listener.startParseTestSets(parseId));
        processTestSets(parseId, catalog.getTestSet());
        emitEvent(listener -> listener.endParseTestSets(parseId));

        // Finish parsing
        emitEvent(listener -> listener.endParseCatalog(parseId));
    }

    private void processGlobalEnvironments(final UUID parseId, final Path catalogFile, final List<Environment> environments) throws ParserException {
        for (final Environment environment : environments) {
            final com.evolvedbinary.xth.tsom.Environment tsomEnvironment = toTsom(catalogFile.toUri(), environment);
            if (tsomEnvironment instanceof final EnvironmentDefinition tsomEnvironmentDefinition) {
                emitEvent(listener -> listener.catalogEnvironment(parseId, tsomEnvironmentDefinition));
            } else {
                throw new ParserException("Unexpected environment reference at catalog level");
            }
        }
    }

    private static com.evolvedbinary.xth.tsom.Environment toTsom(final URI environmentBaseUri, final Environment environment) throws ParserException {
        final String reference = environment.getRef();
        if (reference != null) {
            return EnvironmentReferenceImpl.builder(environmentBaseUri, reference)
                .build();
        }

        final String name = environment.getName();
        final com.evolvedbinary.xth.tsom.EnvironmentDefinition.Builder builder = EnvironmentDefinitionImpl.builder(environmentBaseUri, name);
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
                case Collation collation -> builder.addCollation(toTsom(collation));
                default ->
                    throw new ParserException("Unexpected environment object: " + obj.getClass().getName());
            }
        }

        return builder.build();
    }

    private void processTestSets(final UUID parseId, final List<Catalog.TestSet> testSets) throws IOException, ParserException {
        // TODO(AR) process Test Sets in parallel?
        for (final Catalog.TestSet testSet: testSets) {
            processTestSet(parseId, testSet);
        }
    }

    private void processTestSet(final UUID parseId, final Catalog.TestSet catalogTestSet) throws IOException, ParserException {
        final UUID testSetId = generateUniqueId();
        final Path testSuiteFile = testSuiteDirectory.resolve(catalogTestSet.getFile());
        final TestSet testSet = unmarshalTestSetFile(testSuiteFile);
        final com.evolvedbinary.xth.tsom.TestSet tsomTestSet = toTsom(catalogTestSet.getName(), testSuiteFile, testSet);

        emitEvent(listener -> listener.startParseTestSet(parseId, testSetId, tsomTestSet));
        // TODO(AR) process test cases in parallel?
        for (final TestCase testCase : testSet.getTestCase()) {
            processTestCase(parseId, testSetId, testSuiteFile.toUri(), testCase);
        }
        emitEvent(listener -> listener.endParseTestSet(parseId, testSetId));
    }

    private void processTestCase(final UUID parseId, final UUID testSetId, final URI testCaseBaseUri, final TestCase testCase) throws ParserException {
        final UUID testCaseId = generateUniqueId();
        final com.evolvedbinary.xth.tsom.TestCase tsomTestCase = toTsom(testCaseBaseUri, testCase);
        emitEvent(listener -> listener.testCase(parseId, testSetId, testCaseId, tsomTestCase));
    }

    private static com.evolvedbinary.xth.tsom.TestSet toTsom(final String name, final Path file, final TestSet testSet) throws ParserException {
        final com.evolvedbinary.xth.tsom.TestSet.Builder builder = TestSetImpl.builder(name);
        builder.setFile(file);
        builder.setCovers(testSet.getCovers());
        builder.setCovers30(testSet.getCovers30());
        for (final Object obj : testSet.getDescriptionOrLinkOrEnvironment()) {
            switch (obj) {
                case Description description -> builder.setDescription(description.getValue());
                case Link link -> builder.addLink(toTsom(link));
                case Environment environment -> builder.addEnvironment(toTsom(file.toUri(), environment));
                case Dependency dependency -> builder.addDependency(toTsom(dependency));
                default -> throw new ParserException("Unexpected test-set object: " + obj.getClass().getName());

            }
        }
        return builder.build();
    }

    private TestSet unmarshalTestSetFile(final Path testSuiteFile) throws IOException {
        final Path xmlSchemaFile = testSuiteDirectory.resolve(FOTS31Constants.XML_SCHEMA_FILE_NAME);
        final Path catalogSchemaFile = testSuiteDirectory.resolve(FOTS31Constants.CATALOG_SCHEMA_FILE_NAME);
        return unmarshal(new Path[] { xmlSchemaFile, catalogSchemaFile}, TestSet.class, testSuiteFile);
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Schema toTsom(@Nullable final SchemaType schema) throws ParserException {
        if (schema == null) {
            return null;
        }

        return new com.evolvedbinary.xth.tsom.impl.SchemaImpl(
            schema.getId(),
            schema.getDescription() == null ? null : schema.getDescription().getValue(),
            toTsom(schema.getCreated()),
            toTsom(schema.getModified()),
            toUri(schema.getUri()),
            toUri(schema.getFile()),
            toXsdVersion(schema.getXsdVersion()),
            toTsom(schema.getRole())
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Source toTsom(@Nullable final SourceType source) throws ParserException {
        if (source == null) {
            return null;
        }

        return new com.evolvedbinary.xth.tsom.impl.SourceImpl(
            source.getId(),
            source.getDescription() == null ? null : source.getDescription().getValue(),
            toTsom(source.getCreated()),
            toTsom(source.getModified()),
            toTsom(source.getRole()),
            toUri(source.getFile()),
            toUri(source.getUri()),
            toTsom(source.getValidation())
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Resource toTsom(@Nullable final ResourceType resource) throws ParserException {
        if (resource == null) {
            return null;
        }

        return new com.evolvedbinary.xth.tsom.impl.ResourceImpl(
            resource.getId(),
            resource.getDescription() == null ? null : resource.getDescription().getValue(),
            toTsom(resource.getCreated()),
            toTsom(resource.getModified()),
            toUri(resource.getFile()),
            toUri(resource.getUri()),
            resource.getMediaType(),
            resource.getEncoding()
        );
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

        return new CollectionImpl(
            toUri(collection.getUri()),
            sources,
            resources,
            queries
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable StaticBaseUri toTsom(@Nullable final StaticBaseUri staticBaseUri) throws ParserException {
        if (staticBaseUri == null) {
            return null;
        }

        return new StaticBaseUriImpl(toUri(staticBaseUri.getUri()));
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Collation toTsom(final Collation collation) throws ParserException {
        if (collation == null) {
            return null;
        }

        return new CollationImpl(
            toUri(collation.getUri()),
            collation.isDefault()
        );
    }

    private static @Nullable XsdVersion toXsdVersion(@Nullable final BigDecimal number) throws ParserException {
        if (number == null) {
            return null;

        } else if (number.floatValue() == 1.0f) {
            return XsdVersion.XSD_1_0;

        } else if (number.floatValue() == 1.1f) {
            return XsdVersion.XSD_1_1;

        } else {
            throw new ParserException("Unknown schema version: " + number);
        }
    }

    private static @Nullable ValidationMode toTsom(@Nullable final ValidationEnumType validation) {
        if (validation == null) {
            return null;
        }
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
            .map(FOTS31Parser::toTsom)
            .collect(Collectors.toUnmodifiableList());
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Modified toTsom(@Nullable final Modified modified) {
        if (modified == null) {
            return null;
        }
        return new ModifiedImpl(modified.getBy(), modified.getOn(), modified.getChange());
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

    private static com.evolvedbinary.xth.tsom.@Nullable Link toTsom(@Nullable final Link link) {
        if (link == null) {
            return null;
        }
        return new LinkImpl(toTsom(link.getType()), link.getDocument(), link.getIdref(), link.getSectionNumber());
    }

    private static com.evolvedbinary.xth.tsom.@Nullable DependencyType toTsom(@Nullable final DependencyEnumType dependencyType) {
        if (dependencyType == null) {
            return null;
        }
        return DependencyType.valueOf(dependencyType.name());
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Dependency toTsom(@Nullable final Dependency dependency) throws ParserException {
        if (dependency == null) {
            return null;
        }
        return new DependencyImpl(
            toTsom(dependency.getType()),
            dependency.getValue(),
            dependency.isSatisfied()
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable TestCase toTsom(final URI testCaseBaseUri, @Nullable final TestCase testCase) throws ParserException {
        if (testCase == null) {
            return null;
        }

        final com.evolvedbinary.xth.tsom.TestCase.Builder builder = TestCaseImpl.builder(testCase.getName());
        builder.setCovers(testCase.getCovers());
        builder.setCovers30(testCase.getCovers30());
        builder.setDescription(testCase.getDescription().getValue());
        builder.setCreated(toTsom(testCase.getCreated()));
        for (final Modified modified : testCase.getModified()) {
            builder.addModified(toTsom(modified));
        }
        for (final Object obj : testCase.getEnvironmentOrModuleOrDependency()) {
            switch (obj) {
                case Environment environment -> builder.addEnvironment(toTsom(testCaseBaseUri, environment));
                case Module module -> builder.addModule(toTsom(module));
                case Dependency dependency -> builder.addDependency(toTsom(dependency));
                default -> throw new ParserException("Unexpected test-case object: " + obj.getClass().getName());
            }
        }
        builder.setTest(toTsom(testCase.getTest()));
        builder.setResult(toTsom(testCase.getResult()));
        return builder.build();
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Module toTsom(@Nullable final Module module) throws ParserException {
        if (module == null) {
            return null;
        }
        return new ModuleImpl(
            module.getUri(),
            toUri(module.getLocation()),
            toUri(module.getFile())
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Test toTsom(@Nullable final Test test) throws ParserException {
        if (test == null) {
            return null;
        }
        return new TestImpl(
            test.getValue(),
            toUri(test.getFile())
        );
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Assertion toTsom(@Nullable final Result result) throws ParserException {
        if (result == null) {
            return null;
        }

        final JAXBElement<?> abstractAssertion = result.getAbstractAssertion();
        return toAssertion(abstractAssertion);
    }

    public static com.evolvedbinary.xth.tsom.@Nullable Assertion toAssertion(@Nullable final JAXBElement<?> abstractAssertion) throws ParserException {
        return switch (abstractAssertion.getValue()) {
            case Assert assrt -> toTsom(assrt);
            case AssertEq assertEq -> toTsom(assertEq);
            case AssertCount assertCount -> toTsom(assertCount);
            case AssertDeepEq assertDeepEq -> toTsom(assertDeepEq);
            case AssertPermutation assertPermutation -> toTsom(assertPermutation);
            case AssertXml assertXml -> toTsom(assertXml);
            case SerializationMatches serializationMatches -> toTsom(serializationMatches);
            case AssertSerializationError assertSerializationError -> toTsom(assertSerializationError);
            case AssertType assertType -> toTsom(assertType);
            case AssertStringValue assertStringValue -> toTsom(assertStringValue);
            case Error error -> toTsom(error);
            case SequenceOfAssertionsType sequenceOfAssertionsType -> toTsom(abstractAssertion.getName(), sequenceOfAssertionsType);
            case Element element -> toAssertion(element);
            case Not not -> toTsom(not);
            default -> throw new ParserException("Unexpected assertion object: " + abstractAssertion.getValue().getClass().getName());
        };
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Assertion toAssertion(final Element element) throws ParserException {
        if (ObjectFactory._AssertEmpty_QNAME.getLocalPart().equals(element.getLocalName()) && ObjectFactory._AssertEmpty_QNAME.getNamespaceURI().equals(element.getNamespaceURI())) {
            return AssertEmptyImpl.INSTANCE;

        } else if (ObjectFactory._AssertTrue_QNAME.getLocalPart().equals(element.getLocalName()) && ObjectFactory._AssertTrue_QNAME.getNamespaceURI().equals(element.getNamespaceURI())) {
            return AssertTrueImpl.INSTANCE;

        } else if (ObjectFactory._AssertFalse_QNAME.getLocalPart().equals(element.getLocalName()) && ObjectFactory._AssertFalse_QNAME.getNamespaceURI().equals(element.getNamespaceURI())) {
            return AssertFalseImpl.INSTANCE;
        } else {
            throw new ParserException("Unexpected assertion object: " + element.getLocalName());
        }
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable Assert toTsom(@Nullable final Assert assrt) {
        if (assrt == null) {
            return null;
        }
        return new AssertImpl(assrt.getValue());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertEqual toTsom(@Nullable final AssertEq assertEq) {
        if (assertEq == null) {
            return null;
        }
        return new AssertEqualImpl(assertEq.getValue());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertCount toTsom(@Nullable final AssertCount assertCount) {
        if (assertCount == null) {
            return null;
        }
        return new AssertCountImpl(assertCount.getValue());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertDeepEqual toTsom(@Nullable final AssertDeepEq assertDeepEq) {
        if (assertDeepEq == null) {
            return null;
        }
        return new AssertDeepEqualImpl(assertDeepEq.getValue());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertPermutation toTsom(@Nullable final AssertPermutation assertPermutation) {
        if (assertPermutation == null) {
            return null;
        }
        return new AssertPermutationImpl(assertPermutation.getValue());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertXml toTsom(@Nullable final AssertXml assertXml) throws ParserException {
        if (assertXml == null) {
            return null;
        }
        return new AssertXmlImpl(
                toUri(assertXml.getFile()),
                assertXml.isIgnorePrefixes() == null ? false : assertXml.isIgnorePrefixes(),
                assertXml.getValue()
        );
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertSerializationMatches toTsom(@Nullable final SerializationMatches serializationMatches) throws ParserException {
        if (serializationMatches == null) {
            return null;
        }
        return new AssertSerializationMatchesImpl(
            toUri(serializationMatches.getFile()),
            serializationMatches.getValue(),
            serializationMatches.getFlags()
        );
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertSerializationError toTsom(@Nullable final AssertSerializationError assertSerializationError) {
        if (assertSerializationError == null) {
            return null;
        }
        return new AssertSerializationErrorImpl(assertSerializationError.getCode());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertType toTsom(@Nullable final AssertType assertType) {
        if (assertType == null) {
            return null;
        }
        return new AssertTypeImpl(assertType.getValue());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertStringValue toTsom(@Nullable final AssertStringValue assertStringValue) {
        if (assertStringValue == null) {
            return null;
        }
        return new AssertStringValueImpl(assertStringValue.getValue(), assertStringValue.isNormalizeSpace());
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertError toTsom(@Nullable final Error error) {
        if (error == null) {
            return null;
        }

        if ("*".equals(error.getCode())) {
            return AssertAnyErrorImpl.INSTANCE;
        } else {
            return new AssertErrorCodeImpl(error.getCode());
        }
    }

    private static com.evolvedbinary.xth.tsom.@Nullable Assertion toTsom(final QName sequenceOfAssertionsTypeName, @Nullable final SequenceOfAssertionsType sequenceOfAssertionsType) throws ParserException {
        if (sequenceOfAssertionsType == null) {
            return null;
        }

        final List<JAXBElement<?>> subAssertions = sequenceOfAssertionsType.getAbstractAssertion();
        final List<com.evolvedbinary.xth.tsom.Assertion> subResults = new ArrayList<>(subAssertions.size());
        for (final JAXBElement<?> subAssertion : subAssertions) {
            subResults.add(toAssertion(subAssertion));
        }
        if (ObjectFactory._AllOf_QNAME.equals(sequenceOfAssertionsTypeName)) {
            return new AssertAllOfImpl(subResults);
        } else if (ObjectFactory._AnyOf_QNAME.equals(sequenceOfAssertionsTypeName)) {
            return new AssertAnyOfImpl(subResults);
        } else {
            throw new ParserException("Unexpected sequence of assertions object: " + sequenceOfAssertionsTypeName);
        }
    }

    private static com.evolvedbinary.xth.tsom.assertion.@Nullable AssertNot toTsom(@Nullable final Not not) throws ParserException {
        if (not == null) {
            return null;
        }
        return new AssertNotImpl(toAssertion(not.getAbstractAssertion()));
    }

    private static @Nullable Character firstCharacter(@Nullable final String str) {
        if (str == null) {
            return null;
        }
        return str.charAt(0);
    }

    private static @Nullable URI toUri(@Nullable final String uri) throws ParserException {
        if (uri == null) {
            return null;
        }
        try {
            return new URI(uri);
        } catch (final URISyntaxException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }
}
