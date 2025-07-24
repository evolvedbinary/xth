/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
package com.evolvedbinary.xth.parser.qt3tests;

import com.evolvedbinary.xth.parser.api.AbstractTestSuiteParser;
import com.evolvedbinary.xth.parser.api.ParserConfiguration;
import com.evolvedbinary.xth.parser.api.ParserException;
import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.tsom.EnvironmentDefinition;
import com.evolvedbinary.xth.tsom.DependencyType;
import com.evolvedbinary.xth.tsom.Feature;
import com.evolvedbinary.xth.tsom.SpecificationVersion;
import com.evolvedbinary.xth.tsom.TestSuite;
import com.evolvedbinary.xth.tsom.ValidationMode;
import com.evolvedbinary.xth.tsom.XsdVersion;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertAllOfImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertAnyErrorImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertAnyOfImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertCountImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertDeepEqualImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertEmptyImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertEqualImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertErrorCodeImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertFalseImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertNotImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertPermutationImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertSerializationErrorImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertSerializationMatchesImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertStringValueImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertTrueImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertTypeImpl;
import com.evolvedbinary.xth.tsom.assertion.impl.AssertXmlImpl;
import com.evolvedbinary.xth.tsom.dependency.SpecificationVersionDescription;
import com.evolvedbinary.xth.tsom.dependency.impl.CalendarDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.CollectionStabilityDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.DefaultLanguageDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.DirectoryAsCollectionUriDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.FeatureDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.FormatIntegerSequenceDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.LanguageDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.LimitsDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.SchemaAwareDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.SpecificationDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.SpecificationVersionDescriptionImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.UnicodeNormalizationFormDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.UnicodeVersionDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.XmlVersionDependencyImpl;
import com.evolvedbinary.xth.tsom.dependency.impl.XsdVersionDependencyImpl;
import com.evolvedbinary.xth.tsom.impl.CollationImpl;
import com.evolvedbinary.xth.tsom.impl.CollectionImpl;
import com.evolvedbinary.xth.tsom.impl.ContextItemImpl;
import com.evolvedbinary.xth.tsom.impl.ContextItemRoleImpl;
import com.evolvedbinary.xth.tsom.impl.CreatedImpl;
import com.evolvedbinary.xth.tsom.impl.DecimalFormatImpl;
import com.evolvedbinary.xth.tsom.impl.EnvironmentDefinitionImpl;
import com.evolvedbinary.xth.tsom.impl.EnvironmentReferenceImpl;
import com.evolvedbinary.xth.tsom.impl.FunctionLibraryImpl;
import com.evolvedbinary.xth.tsom.impl.LinkImpl;
import com.evolvedbinary.xth.tsom.impl.ModifiedImpl;
import com.evolvedbinary.xth.tsom.impl.ModuleImpl;
import com.evolvedbinary.xth.tsom.impl.NamespaceImpl;
import com.evolvedbinary.xth.tsom.impl.ParameterImpl;
import com.evolvedbinary.xth.tsom.impl.TestCaseImpl;
import com.evolvedbinary.xth.tsom.impl.TestImpl;
import com.evolvedbinary.xth.tsom.impl.TestSetImpl;
import com.evolvedbinary.xth.tsom.impl.StaticBaseUriImpl;
import com.evolvedbinary.xth.tsom.impl.TestSuiteImpl;
import com.evolvedbinary.xth.tsom.impl.VariableRoleImpl;
import jakarta.xml.bind.JAXBElement;
import org.jspecify.annotations.Nullable;
import org.w3._2010._09.qt_fots_catalog.Assert;
import org.w3._2010._09.qt_fots_catalog.AssertCount;
import org.w3._2010._09.qt_fots_catalog.AssertDeepEq;
import org.w3._2010._09.qt_fots_catalog.AssertEq;
import org.w3._2010._09.qt_fots_catalog.AssertPermutation;
import org.w3._2010._09.qt_fots_catalog.AssertSerializationError;
import org.w3._2010._09.qt_fots_catalog.AssertStringValue;
import org.w3._2010._09.qt_fots_catalog.AssertType;
import org.w3._2010._09.qt_fots_catalog.AssertXml;
import org.w3._2010._09.qt_fots_catalog.Catalog;
import org.w3._2010._09.qt_fots_catalog.Collation;
import org.w3._2010._09.qt_fots_catalog.Collection;
import org.w3._2010._09.qt_fots_catalog.ContextItem;
import org.w3._2010._09.qt_fots_catalog.Created;
import org.w3._2010._09.qt_fots_catalog.DecimalFormat;
import org.w3._2010._09.qt_fots_catalog.Dependency;
import org.w3._2010._09.qt_fots_catalog.DependencyEnumType;
import org.w3._2010._09.qt_fots_catalog.Description;
import org.w3._2010._09.qt_fots_catalog.Environment;
import org.w3._2010._09.qt_fots_catalog.Error;
import org.w3._2010._09.qt_fots_catalog.FunctionLibrary;
import org.w3._2010._09.qt_fots_catalog.Link;
import org.w3._2010._09.qt_fots_catalog.Modified;
import org.w3._2010._09.qt_fots_catalog.Module;
import org.w3._2010._09.qt_fots_catalog.Namespace;
import org.w3._2010._09.qt_fots_catalog.Not;
import org.w3._2010._09.qt_fots_catalog.ObjectFactory;
import org.w3._2010._09.qt_fots_catalog.Param;
import org.w3._2010._09.qt_fots_catalog.ResourceType;
import org.w3._2010._09.qt_fots_catalog.Result;
import org.w3._2010._09.qt_fots_catalog.SequenceOfAssertionsType;
import org.w3._2010._09.qt_fots_catalog.SerializationMatches;
import org.w3._2010._09.qt_fots_catalog.SchemaType;
import org.w3._2010._09.qt_fots_catalog.SourceType;
import org.w3._2010._09.qt_fots_catalog.StaticBaseUri;
import org.w3._2010._09.qt_fots_catalog.Test;
import org.w3._2010._09.qt_fots_catalog.TestCase;
import org.w3._2010._09.qt_fots_catalog.TestSet;
import org.w3._2010._09.qt_fots_catalog.ValidationEnumType;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.evolvedbinary.xth.parser.qt3tests.JAXBUtil.unmarshal;
import static com.evolvedbinary.xth.util.BooleanUtil.parseToBoolean;
import static com.evolvedbinary.xth.util.ListUtil.toImmutableList;
import static com.evolvedbinary.xth.util.SetUtil.toImmutableSet;

public class QT3TestsParser extends AbstractTestSuiteParser implements TestSuiteParser {

    private final ParserConfiguration parserConfiguration;
    private final Path testSuiteDirectory;

    QT3TestsParser(final ParserConfiguration parserConfiguration, final Path testSuiteDirectory) {
        this.parserConfiguration = parserConfiguration;
        this.testSuiteDirectory = testSuiteDirectory;
    }

    @Override
    public String getParserName() {
        return QT3TestsConstants.CATALOG_TEST_SUITE + " " + QT3TestsConstants.CATALOG_VERSION;
    }

    @Override
    public void parse() throws IOException, ParserException {
        final UUID parseId = generateUniqueId();
        final Path xmlSchemaFile = testSuiteDirectory.resolve(QT3TestsConstants.XML_SCHEMA_FILE_NAME);
        final Path catalogSchemaFile = testSuiteDirectory.resolve(QT3TestsConstants.CATALOG_SCHEMA_FILE_NAME);
        final Path catalogFile = testSuiteDirectory.resolve(QT3TestsConstants.CATALOG_FILE_NAME);

        // Start parsing
        emitEvent(listener -> listener.startParseCatalog(parseId, catalogFile, EnumSet.of(SpecificationVersion.XQUERY_3_1, SpecificationVersion.XPATH_3_1)));

        final Catalog catalog = unmarshal(new Path[] { xmlSchemaFile, catalogSchemaFile}, Catalog.class, catalogFile);

        // Process Catalog Environment(s)
        emitEvent(listener -> listener.startParseCatalogEnvironments(parseId));
        processGlobalEnvironments(parseId, catalogFile, catalog.getEnvironment());
        emitEvent(listener -> listener.endParseCatalogEnvironments(parseId));

        // Process Catalog TestSet(s)
        final TestSuite testSuite = TestSuiteImpl.builder(parseId, catalog.getTestSuite(), catalog.getVersion())
                        .setFile(catalogFile)
                        .build();
        emitEvent(listener -> listener.startParseTestSets(parseId, testSuite));
        processTestSets(parseId, catalog.getTestSet());
        emitEvent(listener -> listener.endParseTestSets(parseId, testSuite));

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
        // TODO(AR) process Test Sets in parallel? it should be the case that OTR events can cope with this already
        for (final Catalog.TestSet testSet: testSets) {
            processTestSet(parseId, testSet);
        }
    }

    private void processTestSet(final UUID parseId, final Catalog.TestSet catalogTestSet) throws IOException, ParserException {
        final UUID testSetInstanceId = generateUniqueId();
        final Path testSuiteFile = testSuiteDirectory.resolve(catalogTestSet.getFile());

        final ParserAction testSetParserAction = getTestSetParserAction(catalogTestSet.getName());
        if (testSetParserAction == ParserAction.EXCLUDE) {
            emitEvent(listener -> listener.excludedTestSet(parseId, testSetInstanceId, testSuiteDirectory.resolve(catalogTestSet.getFile()), catalogTestSet.getName()));

        } else if (testSetParserAction == ParserAction.SKIP) {
            emitEvent(listener -> listener.skippedTestSet(parseId, testSetInstanceId, testSuiteDirectory.resolve(catalogTestSet.getFile()), catalogTestSet.getName()));

        } else {

            final TestSet testSet = unmarshalTestSetFile(testSuiteFile);
            final com.evolvedbinary.xth.tsom.TestSet tsomTestSet = toTsom(testSetInstanceId, catalogTestSet.getName(), testSuiteFile, testSet);

            emitEvent(listener -> listener.startParseTestSet(parseId, testSetInstanceId, tsomTestSet));
            // TODO(AR) process test cases in parallel?
            for (final TestCase testCase : testSet.getTestCase()) {
                processTestCase(parseId, testSetInstanceId, testSuiteFile.toUri(), testCase);
            }
            emitEvent(listener -> listener.endParseTestSet(parseId, testSetInstanceId));
        }
    }

    private void processTestCase(final UUID parseId, final UUID testSetInstanceId, final URI testCaseBaseUri, final TestCase testCase) throws ParserException {
        final UUID testCaseInstanceId = generateUniqueId();

        final ParserAction testCaseParserAction = getTestCaseParserAction(testCase.getName());
        if (testCaseParserAction == ParserAction.EXCLUDE) {
            emitEvent(listener -> listener.excludedTestCase(parseId, testSetInstanceId, testCaseInstanceId, testCase.getName()));

        } else if (testCaseParserAction == ParserAction.SKIP) {
            emitEvent(listener -> listener.skippedTestCase(parseId, testSetInstanceId, testCaseInstanceId, testCase.getName()));

        } else {

            final com.evolvedbinary.xth.tsom.TestCase tsomTestCase = toTsom(testCaseInstanceId, testCaseBaseUri, testCase);
            emitEvent(listener -> listener.testCase(parseId, testSetInstanceId, testCaseInstanceId, tsomTestCase));
        }
    }

    private static com.evolvedbinary.xth.tsom.TestSet toTsom(final UUID testSetInstanceId, final String name, final Path file, final TestSet testSet) throws ParserException {
        final com.evolvedbinary.xth.tsom.TestSet.Builder builder = TestSetImpl.builder(testSetInstanceId, name);
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
        final Path xmlSchemaFile = testSuiteDirectory.resolve(QT3TestsConstants.XML_SCHEMA_FILE_NAME);
        final Path catalogSchemaFile = testSuiteDirectory.resolve(QT3TestsConstants.CATALOG_SCHEMA_FILE_NAME);
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
            QName.valueOf(parameter.getName().getLocalPart()),
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
            toImmutableList(sources),
            toImmutableList(resources),
            toImmutableList(queries)
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
            .map(QT3TestsParser::toTsom)
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
            return ContextItemRoleImpl.INSTANCE;
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

    private static com.evolvedbinary.xth.tsom.@Nullable Dependency<?> toTsom(@Nullable final Dependency dependency) throws ParserException {
        if (dependency == null) {
            return null;
        }

        return switch (dependency.getType()) {
            case DependencyEnumType.CALENDAR -> new CalendarDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.COLLECTION_STABILITY -> new CollectionStabilityDependencyImpl(parseToBoolean(dependency.getValue()), dependency.isSatisfied());
            case DependencyEnumType.DEFAULT_LANGUAGE -> new DefaultLanguageDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.DIRECTORY_AS_COLLECTION_URI -> new DirectoryAsCollectionUriDependencyImpl(parseToBoolean(dependency.getValue()), dependency.isSatisfied());
            case DependencyEnumType.FEATURE -> new FeatureDependencyImpl(toFeatures(dependency.getValue()), dependency.isSatisfied());
            case DependencyEnumType.FORMAT_INTEGER_SEQUENCE -> new FormatIntegerSequenceDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.LANGUAGE -> new LanguageDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.LIMITS -> new LimitsDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.SPEC -> new SpecificationDependencyImpl(toSpecificationVersions(dependency.getValue()), dependency.isSatisfied());
            case DependencyEnumType.SCHEMA_AWARE -> new SchemaAwareDependencyImpl(parseToBoolean(dependency.getValue()), dependency.isSatisfied());
            case DependencyEnumType.UNICODE_NORMALIZATION_FORM -> new UnicodeNormalizationFormDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.UNICODE_VERSION -> new UnicodeVersionDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.XML_VERSION -> new XmlVersionDependencyImpl(dependency.getValue(), dependency.isSatisfied());
            case DependencyEnumType.XSD_VERSION -> new XsdVersionDependencyImpl(dependency.getValue(), dependency.isSatisfied());
        };
    }

    private static Set<SpecificationVersionDescription> toSpecificationVersions(final String specificationsString) {
        final String[] parts = specificationsString.trim().split(" ");
        final Set<SpecificationVersionDescription> specificationVersions = new LinkedHashSet<>();
        for (final String part : parts) {
            specificationVersions.add(SpecificationVersionDescriptionImpl.fromFotsName(part));
        }
        return toImmutableSet(specificationVersions);
    }

    private static Set<Feature> toFeatures(final String featuresString) {
        final String[] parts = featuresString.trim().split(" ");
        final List<Feature> features = new ArrayList<>();
        for (final String part : parts) {
            features.add(Feature.fromFotsName(part));
        }
        return EnumSet.copyOf(features);
    }

    private static com.evolvedbinary.xth.tsom.@Nullable TestCase toTsom(final UUID testCaseInstanceId, final URI testCaseBaseUri, @Nullable final TestCase testCase) throws ParserException {
        if (testCase == null) {
            return null;
        }

        final com.evolvedbinary.xth.tsom.TestCase.Builder builder = TestCaseImpl.builder(testCaseInstanceId, testCase.getName());
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
            return new AssertAllOfImpl(toImmutableList(subResults));
        } else if (ObjectFactory._AnyOf_QNAME.equals(sequenceOfAssertionsTypeName)) {
            return new AssertAnyOfImpl(toImmutableList(subResults));
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

    private ParserAction getTestSetParserAction(final String testSetName) {
        return getParserAction(testSetName, parserConfiguration.excludeTestSets(), parserConfiguration.skipTestSets(), parserConfiguration.testSets(), parserConfiguration.testSetPattern());
    }

    private ParserAction getTestCaseParserAction(final String testCaseName) {
        return getParserAction(testCaseName, parserConfiguration.excludeTestCases(), parserConfiguration.skipTestCases(), parserConfiguration.testCases(), parserConfiguration.testCasePattern());
    }

    private static ParserAction getParserAction(final String name, @Nullable final String[] excludedNames, @Nullable final String[] skippedNames, @Nullable final String[] allowedNames, @Nullable final Pattern allowedNamePattern) {
        if (excludedNames != null) {
            for (final String excludeTestSet : excludedNames) {
                if (name.matches(excludeTestSet)) {
                    return ParserAction.EXCLUDE;
                }
            }
        }

        if (skippedNames != null) {
            for (final String skippedTestSet : skippedNames) {
                if (name.matches(skippedTestSet)) {
                    return ParserAction.SKIP;
                }
            }
        }

        if (allowedNames == null) {
            if (allowedNamePattern == null) {
                return ParserAction.PARSE;
            }
        } else {
            for (final String allowedTestSet : allowedNames) {
                if (name.matches(allowedTestSet)) {
                    return ParserAction.PARSE;
                }
            }
        }

        final boolean allowedByPattern;
        if (allowedNamePattern == null) {
            allowedByPattern = false;
        } else {
            allowedByPattern = allowedNamePattern.matcher(name).matches();
        }

        return allowedByPattern ? ParserAction.PARSE : ParserAction.EXCLUDE;
    }

    private enum ParserAction {
        PARSE,
        SKIP,
        EXCLUDE
    }
}
