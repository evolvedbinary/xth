package com.evolvedbinary.xth.connector.saxon;

import com.evolvedbinary.xth.connector.api.Connector;

import com.evolvedbinary.xth.connector.api.ConnectorException;
import com.evolvedbinary.xth.tsom.*;
import com.evolvedbinary.xth.tsom.assertion.*;
import com.evolvedbinary.xth.tsom.Collection;
import com.evolvedbinary.xth.tsom.ValidationMode;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultErrorImpl;
import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultFailureImpl;
import com.evolvedbinary.xth.tsom.result.impl.TestCaseResultPassImpl;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import net.sf.saxon.Configuration;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.expr.sort.AtomicSortComparer;
import net.sf.saxon.expr.sort.CodepointCollator;
import net.sf.saxon.lib.ErrorReporter;
import net.sf.saxon.lib.ResourceCollection;
import net.sf.saxon.lib.StringCollator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.resource.XmlResource;
import net.sf.saxon.s9api.*;
import net.sf.saxon.trans.DecimalFormatManager;
import net.sf.saxon.trans.DecimalSymbols;
import net.sf.saxon.trans.NoDynamicContextException;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.Whitespace;
import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.evolvedbinary.xth.util.IOUtil.path;
import static com.evolvedbinary.xth.util.IOUtil.readFileContent;
import static com.evolvedbinary.xth.util.ListUtil.safeAdd;
import static com.evolvedbinary.xth.util.MapUtil.safePut;

@ThreadSafe
public class SaxonConnector implements Connector {

    private static final net.sf.saxon.s9api.QName RESULT_QNAME = new net.sf.saxon.s9api.QName("result");

    private Processor processor;
    private XPathCompiler assertXpathCompiler;

    /**
     * Environments declared globally.
     * Key is the Environment's name.
     * Value is Environment.
     */
    private Map<String, EnvironmentDefinition> globalEnvironments;

    /**
     * Environments declared for each Test Set.
     * Key is the Test Set's name.
     * Value is a map:
     *  Key is the Environment's name
     *  Value is the Environment
     */
    @GuardedBy("testSetEnvironmentsSetupLocks")
    private final Map<String, Map<String, EnvironmentDefinition>> testSetEnvironments = new ConcurrentHashMap<>();
    private final Map<String, AtomicReference<EnvironmentSetupState>> testSetEnvironmentsSetupLocks = new ConcurrentHashMap<>();
    private Path baseUri;

    @Override
    public String getConnectorName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getImplementationName() throws ConnectorException {
        if (this.processor == null) {
            throw new ConnectorException("Connection must be initialized first");
        }
        return this.processor.getSaxonEdition();
    }

    @Override
    public String getImplementationVersion() throws ConnectorException {
        if (this.processor == null) {
            throw new ConnectorException("Connection must be initialized first");
        }
        return this.processor.getSaxonProductVersion();
    }

    @Override
    public void initialize(final Path baseUri, final List<EnvironmentDefinition> globalEnvironments) {
        this.baseUri = baseUri;
        this.globalEnvironments = new HashMap<>(globalEnvironments.size());
        for (final EnvironmentDefinition globalEnvironment : globalEnvironments) {
            this.globalEnvironments.put(globalEnvironment.getName(), globalEnvironment);
        }

        // TODO(AR) should we allow Saxon configuration to be specified?
        final Configuration configuration = Configuration.newLicensedConfiguration();
        this.processor = new Processor(configuration);

        // Set up an XPath Compiler for evaluating assertions
        this.assertXpathCompiler = processor.newXPathCompiler();
        this.assertXpathCompiler.setLanguageVersion("3.1");  // TODO(AR) make this configurable based on the test suite version
//        assertXpc.declareNamespace("fn", NamespaceConstant.FN);
//        assertXpc.declareNamespace("xs", NamespaceConstant.SCHEMA);
//        assertXpc.declareNamespace("math", NamespaceConstant.MATH);
//        assertXpc.declareNamespace("map", NamespaceConstant.MAP_FUNCTIONS);
        this.assertXpathCompiler.declareVariable(RESULT_QNAME);
    }

    @Override
    public TestCaseResult executeTestCase(final TestSet testSet, final TestCase testCase) throws ConnectorException {
        final XQueryCompiler xqueryCompiler = processor.newXQueryCompiler();

        final Map<String, EnvironmentDefinition> testSetEnvironments = getTestSetEnvironments(testSet);
        final List<EnvironmentDefinition> testCaseEnvironments = getTestCaseEnvironments(testSetEnvironments, testCase);

        // Set up the static context, and get the dynamic context
        final DynamicContextInfo dynamicContextInfo;
        try {
            dynamicContextInfo = setupEnvironments(xqueryCompiler, testCaseEnvironments);
        } catch (final SaxonApiException | XPathException | IOException | URISyntaxException e) {
            throw new ConnectorException(String.format("Error when setting up environments for test case: %s#%s", testSet.getName(), testCase.getName()), e);
        }

        // Get the test query
        final String xqueryTest;
        try {
            xqueryTest = getContent(testCase.getTest());
        } catch (final IOException e) {
            throw new ConnectorException(String.format("Error when loading query file for test case: %s#%s due to: %s", testSet.getName(), testCase.getName(), e.getMessage()), e);
        }

        // Compile the test query
        final XQueryExecutable xqueryExecutable;
        long testCompilationEndTime;
        final long testCompilationStartTime = System.currentTimeMillis();
        try {
            xqueryExecutable = xqueryCompiler.compile(xqueryTest);
            testCompilationEndTime = System.currentTimeMillis();
        } catch (final SaxonApiException e) {
            testCompilationEndTime = System.currentTimeMillis();
            if (e.getCause() instanceof XPathException) {
                // XPathException occurred during compilation, check if it passes the Test Case's assertion
                final long testCompilationDuration = testCompilationEndTime - testCompilationStartTime;
                final EvaluationResult evaluationResult = EvaluationResult.failure(e, null);
                return testAssertion(testCase, evaluationResult, testCompilationDuration);
            } else {
                throw new ConnectorException(String.format("Error when compiling test case: %s#%s due to: %s", testSet.getName(), testCase.getName(), e.getMessage()), e);
            }
        }

        // Load the test query and set the Dynamic Context
        final XQueryEvaluator xqueryEvaluator = xqueryExecutable.load();
        try {
            setupDynamicContext(xqueryEvaluator, dynamicContextInfo);
        } catch (final SaxonApiException e) {
            throw new ConnectorException(String.format("Error when setting dynamic context of test case: %s#%s due to: %s", testSet.getName(), testCase.getName(), e.getMessage()), e);
        }

        // Evaluate the test query
        EvaluationResult evaluationResult;
        final FotsErrorReporter errorReporter = new FotsErrorReporter();
        xqueryEvaluator.setErrorReporter(errorReporter);
        long testExecutionEndTime;
        final long testExecutionStartTime = System.currentTimeMillis();
        try {
            final XdmValue result = xqueryEvaluator.evaluate();
            testExecutionEndTime = System.currentTimeMillis();
            evaluationResult = EvaluationResult.success(result);
        } catch (final SaxonApiException e) {
            testExecutionEndTime = System.currentTimeMillis();
            evaluationResult = EvaluationResult.failure(e, errorReporter.errors);
        }

        // Test the assertion against the result of the query
        final long testDuration = (testCompilationEndTime - testCompilationStartTime) + (testExecutionEndTime - testExecutionStartTime);
        return testAssertion(testCase, evaluationResult, testDuration);
    }

    private TestCaseResult testAssertion(final TestCase testCase, final EvaluationResult evaluationResult, final long testDuration) throws ConnectorException {
        try {
            final Assertion assertion = testCase.getResult();
            final boolean assertionHolds = testAssertion(assertion, evaluationResult);
            if (assertionHolds) {
                return new TestCaseResultPassImpl(testDuration);
            } else {
                // TODO(AR) add failure information
                return new TestCaseResultFailureImpl(testDuration);
            }
        } catch (final SaxonApiException e) {
            // TODO(AR) add error information
            return new TestCaseResultErrorImpl(testDuration);
        }
    }

    private boolean testAssertion(final Assertion assertion, final EvaluationResult actualResult) throws ConnectorException, SaxonApiException {
        return switch (assertion) {
            case Assert assrt -> testAssert(assrt, actualResult);
            case AssertEqual assertEqual -> testAssertEqual(assertEqual, actualResult);
            case AssertCount assertCount -> testAssertCount(assertCount, actualResult);
            case AssertDeepEqual assertDeepEqual -> testAssertDeepEqual(assertDeepEqual, actualResult);
            case AssertPermutation assertPermutation -> testAssertPermutation(assertPermutation, actualResult);
            case AssertXml assertXml -> testAssertXml(assertXml, actualResult);
            case AssertSerializationMatches assertSerializationMatches -> testAssertSerializationMatches(assertSerializationMatches, actualResult);
            case AssertSerializationError assertSerializationError -> testAssertSerializationError(assertSerializationError, actualResult);
            case AssertEmpty assertEmpty -> testAssertEmpty(assertEmpty, actualResult);
            case AssertType assertType -> testAssertType(assertType, actualResult);
            case AssertTrue assertTrue -> testAssertTrue(assertTrue, actualResult);
            case AssertFalse assertFalse -> testAssertFalse(assertFalse, actualResult);
            case AssertStringValue assertStringValue -> testAssertStringValue(assertStringValue, actualResult);
            case AssertError assertError -> testAssertError(assertError, actualResult);
            case AssertAnyOf assertAnyOf -> testAssertAnyOf(assertAnyOf, actualResult);
            case AssertAllOf assertAllOf -> testAssertAllOf(assertAllOf, actualResult);
            case AssertNot assertNot -> !testAssertion(assertNot.getAssertion(), actualResult);
        };
    }

    private boolean testAssert(final Assert assrt, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> evaluateAssertion(assrt.getXpathExpression(), actualResultIsSuccess.value);
        };
    }

    private boolean testAssertEqual(final AssertEqual assertEqual, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> evaluateAssertion(String.format("$result eq %s", assertEqual.getXpathExpression()), actualResultIsSuccess.value);
        };
    }

    private boolean testAssertCount(final AssertCount assertCount, final EvaluationResult actualResult) {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> assertCount.getCount().equals(BigInteger.valueOf(actualResultIsSuccess.value.size()));
        };
    }

    private boolean testAssertDeepEqual(final AssertDeepEqual assertDeepEqual, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> evaluateAssertion(String.format("deep-equal($result , (%s))", assertDeepEqual.getSequence()), actualResultIsSuccess.value);
        };
    }

    private boolean testAssertPermutation(final AssertPermutation assertPermutation, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> testAssertPermutationSuccess(assertPermutation, actualResultIsSuccess);
        };
    }

    private boolean testAssertXml(final AssertXml assertXml, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            // TODO(AR) need to account for assertXml.isIgnorePrefixes()
            case EvaluationResultSuccess actualResultIsSuccess -> evaluateAssertion(String.format("deep-equal($result , %s)", assertXml.getXml()), actualResultIsSuccess.value);
        };
    }

    private boolean testAssertPermutationSuccess(final AssertPermutation assertPermutation, final EvaluationResultSuccess actualResultSuccess) throws SaxonApiException {
        final XPathSelector xpathSelector = assertXpathCompiler.compile(String.format("(%s)", assertPermutation.getSequence())).load();
        xpathSelector.setVariable(RESULT_QNAME, XdmEmptySequence.getInstance());  // not used

        final StringCollator collator = CodepointCollator.getInstance();
        try {
            // calculate expected items
            int expectedItems = 0;
            final Set<Object> expected = new HashSet<>();
            final XdmValue assertPermutationValue = xpathSelector.evaluate();
            for (final XdmItem assertPermutationItem : assertPermutationValue) {
                expectedItems++;
                final AtomicValue value = (AtomicValue) assertPermutationItem.getUnderlyingValue();
                final Object comparable = value.isNaN() ? AtomicSortComparer.COLLATION_KEY_NaN : value.getXPathComparable(collator, 0);
                expected.add(comparable);
            }

            // calculate actual items
            int actualItems = 0;
            for (final XdmItem actualResultItem : actualResultSuccess.value) {
                actualItems++;
                final AtomicValue value = (AtomicValue) actualResultItem.getUnderlyingValue();
                final Object comparable = value.isNaN() ? AtomicSortComparer.COLLATION_KEY_NaN : value.getXPathComparable(collator, 0);
                if (!expected.contains(comparable)) {
                    return false;
                }
            }

            return actualItems == expectedItems;
        } catch (final NoDynamicContextException e) {
            throw new SaxonApiException(e.getMessage(), e);
        }
    }

    private boolean testAssertSerializationMatches(final AssertSerializationMatches assertSerializationMatches, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> testAssertSerializationMatchesSuccess(assertSerializationMatches, actualResultIsSuccess);
        };
    }

    private boolean testAssertSerializationMatchesSuccess(final AssertSerializationMatches assertSerializationMatches, final EvaluationResultSuccess actualResultSuccess) throws SaxonApiException {
        if (assertSerializationMatches.getRegularExpressionFlags() != null) {
            return evaluateAssertion(String.format("matches($result , '%s', '%s')", assertSerializationMatches.getRegularExpression(), assertSerializationMatches.getRegularExpressionFlags()), actualResultSuccess.value);
        } else {
            return evaluateAssertion(String.format("matches($result , '%s')", assertSerializationMatches.getRegularExpression()), actualResultSuccess.value);
        }
    }

    private boolean testAssertSerializationError(final AssertSerializationError assertSerializationError, final EvaluationResult actualResult) {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> testAssertSerializationErrorSuccess(assertSerializationError, actualResultIsSuccess);
        };
    }

    private boolean testAssertSerializationErrorSuccess(final AssertSerializationError assertSerializationError, final EvaluationResultSuccess actualResultSuccess) {
        final Serializer serializer = assertXpathCompiler.getProcessor().newSerializer();
        serializer.setOutputProperty(Serializer.Property.METHOD, "xml");
        serializer.setOutputProperty(Serializer.Property.INDENT, "no");
        serializer.setOutputProperty(Serializer.Property.OMIT_XML_DECLARATION, "yes");
        try {
            serializer.serializeXdmValue(actualResultSuccess.value);
            return false;
        } catch (final SaxonApiException err) {
            return assertSerializationError.getCode().equals(err.getErrorCode().getLocalName());
        }
    }

    private boolean testAssertEmpty(final AssertEmpty assertEmpty, final EvaluationResult actualResult) {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> actualResultIsSuccess.value.isEmpty();
        };
    }

    private boolean testAssertType(final AssertType assertType, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> evaluateAssertion(String.format("$result instance of %s", assertType.getType()), actualResultIsSuccess.value);
        };
    }

    private boolean testAssertTrue(final AssertTrue assertTrue, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> actualResultIsSuccess.value.size() == 1 &&
                actualResultIsSuccess.value.itemAt(0).isAtomicValue() &&
                ((XdmAtomicValue) actualResultIsSuccess.value.itemAt(0)).getPrimitiveTypeName().equals(net.sf.saxon.s9api.QName.XS_BOOLEAN) &&
                ((XdmAtomicValue) actualResultIsSuccess.value.itemAt(0)).getBooleanValue();
        };
    }

    private boolean testAssertFalse(final AssertFalse assertFalse, final EvaluationResult actualResult) throws SaxonApiException {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> actualResultIsSuccess.value.size() == 1 &&
                actualResultIsSuccess.value.itemAt(0).isAtomicValue() &&
                ((XdmAtomicValue) actualResultIsSuccess.value.itemAt(0)).getPrimitiveTypeName().equals(net.sf.saxon.s9api.QName.XS_BOOLEAN) &&
                !((XdmAtomicValue) actualResultIsSuccess.value.itemAt(0)).getBooleanValue();
        };
    }

    private boolean testAssertStringValue(final AssertStringValue assertStringValue, final EvaluationResult actualResult) {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> false;
            case EvaluationResultSuccess actualResultIsSuccess -> testAssertStringValueSuccess(assertStringValue, actualResultIsSuccess);
        };
    }

    private boolean testAssertStringValueSuccess(final AssertStringValue assertStringValue, final EvaluationResultSuccess actualResultSuccess) {
        String resultString;
        if (actualResultSuccess.value instanceof XdmItem) {
            resultString = ((XdmItem) actualResultSuccess.value).getStringValue();
        } else {
            boolean first = true;
            final StringBuilder stringBuilder = new StringBuilder();
            for (final XdmItem actualResultItem : actualResultSuccess.value) {
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(actualResultItem.getStringValue());
            }
            resultString = stringBuilder.toString();
        }

        String assertionString = assertStringValue.getStringValue();
        if (assertStringValue.isNormalizeSpace()) {
            assertionString = Whitespace.collapseWhitespace(assertionString).toString();
            resultString = Whitespace.collapseWhitespace(resultString).toString();
        }

        return resultString.equals(assertionString);
    }

    private boolean testAssertError(final AssertError assertError, final EvaluationResult actualResult) {
        return switch (actualResult) {
            case EvaluationResultFailure actualResultIsFailure -> switch(assertError) {
                case AssertAnyError assertAnyError -> true;
                case AssertErrorCode assertErrorCode -> testAssertErrorCode(assertErrorCode, actualResultIsFailure);
            };
            case EvaluationResultSuccess actualResultIsSuccess -> false;
        };
    }

    private boolean testAssertErrorCode(final AssertErrorCode assertError, final EvaluationResultFailure actualResultFailure) {
        net.sf.saxon.s9api.QName actualErrorCode = actualResultFailure.exception.getErrorCode();
        if (actualErrorCode != null && assertError.getCode().equals(actualErrorCode.getLocalName())) {
            return true;
        }

        if (actualResultFailure.errorsReported != null) {
            for (final ErrorInfo errorInfo : actualResultFailure.errorsReported) {
                actualErrorCode = errorInfo.code;
                if (actualErrorCode != null && assertError.getCode().equals(actualErrorCode.getLocalName())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean testAssertAnyOf(final AssertAnyOf assertAnyOf, final EvaluationResult actualResult) throws ConnectorException, SaxonApiException {
        for (final Assertion assertion : assertAnyOf.getAssertions()) {
            if (testAssertion(assertion, actualResult)) {
                return true;
            }
        }
        return false;
    }

    private boolean testAssertAllOf(final AssertAllOf assertAllOf, final EvaluationResult actualResult) throws ConnectorException, SaxonApiException {
        for (final Assertion assertion : assertAllOf.getAssertions()) {
            if (!testAssertion(assertion, actualResult)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateAssertion(final String assertionXpath, final XdmValue actualResult) throws SaxonApiException {
        final XPathSelector xpathSelector = assertXpathCompiler.compile(assertionXpath).load();
        xpathSelector.setVariable(RESULT_QNAME, actualResult);
        return ((XdmAtomicValue) xpathSelector.evaluateSingle()).getBooleanValue();
    }

    private static void setupDynamicContext(final XQueryEvaluator xqueryEvaluator, final DynamicContextInfo dynamicContextInfo) throws SaxonApiException {
        // Set the External Variables
        if (dynamicContextInfo.variables != null) {
            for (final Map.Entry<net.sf.saxon.s9api.QName, XdmValue> variable : dynamicContextInfo.variables.entrySet()) {
                xqueryEvaluator.setExternalVariable(variable.getKey(), variable.getValue());
            }
        }

        // Set the Context Item
        if (dynamicContextInfo.contextItem != null) {
            xqueryEvaluator.setContextItem(dynamicContextInfo.contextItem);
        }

        // Set the URI Resolver
        xqueryEvaluator.setURIResolver(new FotsUriResolver(dynamicContextInfo.sourceDocuments));
    }

    private DynamicContextInfo setupEnvironments(final XQueryCompiler xqueryCompiler, final List<EnvironmentDefinition> testCaseEnvironments) throws SaxonApiException, ConnectorException, XPathException, IOException, URISyntaxException {
        Map<String, XdmNode> sourceDocuments = null;
        XdmItem contextItem = null;
        Map<net.sf.saxon.s9api.QName, XdmValue> variables = null;
        List<NameAndType> declaredExternalVariables = null;
        Map<QName, Map<String, String>> decimalFormats = null;

        final XPathCompiler xpathCompiler = xqueryCompiler.getProcessor().newXPathCompiler();
        xpathCompiler.setLanguageVersion("3.0");  // TODO(AR) parameterise the version - should be determined from the test suite
        xpathCompiler.setCaching(true);
        final SchemaManager schemaManager = xqueryCompiler.getProcessor().getSchemaManager();
        final DocumentBuilder documentBuilder = xqueryCompiler.getProcessor().newDocumentBuilder();

        for (final EnvironmentDefinition testCaseEnvironment : testCaseEnvironments) {

            // Configure Schemas
            if (schemaManager != null) {
                for (final Schema schema : testCaseEnvironment.getSchemas()) {
                    final Path schemaPath = path(testCaseEnvironment.getBaseUri(), schema.getFile());
                    schemaManager.load(new StreamSource(schemaPath.toFile()));
                }
            } else if (!testCaseEnvironments.isEmpty()) {
                // TODO(AR) warn that we have schemas but there is no schema manager
            }

            // Configure Source documents
            for (final Source source : testCaseEnvironment.getSources()) {
                final Path sourcePath = path(testCaseEnvironment.getBaseUri(), source.getFile());

                // set validation of source document
                if (source.getValidationMode() == ValidationMode.SKIP) {
                     documentBuilder.setSchemaValidator(null);
                } else {
                    if (schemaManager != null) {
                        final SchemaValidator schemaValidator = schemaManager.newSchemaValidator();
                        schemaValidator.setLax(source.getValidationMode() == ValidationMode.LAX);
                        documentBuilder.setSchemaValidator(schemaValidator);
                        xqueryCompiler.setSchemaAware(true);
                    } else if (!testCaseEnvironments.isEmpty()) {
                        // TODO(AR) warn that we have schemas but there is no schema manager
                    }
                }

                final XdmNode sourceDocument = documentBuilder.build(sourcePath.toFile());
                if (source.getUri() != null) {
                    sourceDocuments = safePut(sourceDocuments, source.getUri().toString(), sourceDocument);
                }

                if (source.getRole() instanceof ContextItemRole) {
                    contextItem = sourceDocument;
                } else if (source.getRole() instanceof final VariableRole variableRole) {
                    final net.sf.saxon.s9api.QName variableName = new net.sf.saxon.s9api.QName(variableRole.getName());
                    variables = safePut(variables, variableName, sourceDocument);
                    declaredExternalVariables = safeAdd(declaredExternalVariables, new NameAndType(variableName));
                }
            }

            for (final Resource resource : testCaseEnvironment.getResources()) {
                throw new UnsupportedOperationException("TODO(AR) implement environment resources");
//                xqueryCompiler.setResource(resource);
            }

            // Configure Variables
            for (final Parameter parameter : testCaseEnvironment.getParameters()) {
                final XdmValue value;
                if (parameter.getSource() != null) {
                    final XdmNode sourceDocument = sourceDocuments != null ? sourceDocuments.get(parameter.getSource()) : null;
                    if (sourceDocument == null) {
                        throw new ConnectorException(String.format("Could not locate source document: %s when setting param: %s", parameter.getSource(), parameter.getName()));
                    }
                    value = sourceDocument;
                } else {
                    value = xpathCompiler.evaluate(parameter.getSelect(), null);
                }

                final net.sf.saxon.s9api.QName parameterQName = new net.sf.saxon.s9api.QName(parameter.getName());
                variables = safePut(variables, parameterQName, value);
                if (parameter.isDeclared()) {
                    declaredExternalVariables = safeAdd(declaredExternalVariables, new NameAndType(parameterQName, parameter.getAs()));
                }
            }

            // Configure Context Item
            if (testCaseEnvironment.getContextItem() != null) {
                final XdmValue value = xpathCompiler.evaluate(testCaseEnvironment.getContextItem().getSelect(), null);
                if (value.size() > 1) {
                    throw new ConnectorException("Context Item evaluates to more than one value");
                }
                contextItem = value.itemAt(0);
            }

            // Configure Decimal Formats
            final DecimalFormatManager decimalFormatManager = xpathCompiler.getUnderlyingStaticContext().getDecimalFormatManager();
            for (final DecimalFormat decimalFormat : testCaseEnvironment.getDecimalFormats()) {

                final QName formatName = decimalFormat.getName();
                final StructuredQName formatStructuredName = new StructuredQName(decimalFormat.getName().getPrefix(), decimalFormat.getName().getPrefix(), decimalFormat.getName().getLocalPart());

                final DecimalSymbols symbols;
                if (DecimalFormat.DEFAULT_DECIMAL_FORMAT_NAME.equals(formatName)) {
                    symbols = decimalFormatManager.getDefaultDecimalFormat();
                } else {
                    symbols = decimalFormatManager.obtainNamedDecimalFormat(formatStructuredName);
                }

                Map<String, String> decimalFormatProperties = null;

                if (decimalFormat.getDecimalSeparator() != null) {
                    final String value = decimalFormat.getDecimalSeparator().toString();
                    symbols.setDecimalSeparator(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "decimal-separator", value);
                }

                if (decimalFormat.getGroupingSeparator() != null) {
                    final String value = decimalFormat.getGroupingSeparator().toString();
                    symbols.setGroupingSeparator(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "grouping-separator", value);
                }

                if (decimalFormat.getZeroDigit() != null) {
                    final String value = decimalFormat.getZeroDigit().toString();
                    symbols.setZeroDigit(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "zero-digit", value);
                }

                if (decimalFormat.getDigit() != null) {
                    final String value = decimalFormat.getDigit().toString();
                    symbols.setDigit(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "digit", value);
                }

                if (decimalFormat.getMinusSign() != null) {
                    final String value = decimalFormat.getMinusSign().toString();
                    symbols.setMinusSign(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "minus-sign", value);
                }

                if (decimalFormat.getPercent() != null) {
                    final String value = decimalFormat.getPercent().toString();
                    symbols.setPercent(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "percent", value);
                }

                if (decimalFormat.getPerMille() != null) {
                    final String value = decimalFormat.getPerMille().toString();
                    symbols.setPerMille(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "per-mille", value);
                }

                if (decimalFormat.getPatternSeparator() != null) {
                    final String value = decimalFormat.getPatternSeparator().toString();
                    symbols.setPatternSeparator(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "pattern-separator", value);
                }

                if (decimalFormat.getExponentSeparator() != null) {
                    final String value = decimalFormat.getExponentSeparator().toString();
                    symbols.setExponentSeparator(value);
                    decimalFormatProperties = safePut(decimalFormatProperties, "exponent-separator", value);
                }

                if (decimalFormat.getInfinity() != null) {
                    symbols.setInfinity(decimalFormat.getInfinity());
                    decimalFormatProperties = safePut(decimalFormatProperties, "infinity", decimalFormat.getInfinity());
                }

                if (decimalFormat.getNaN() != null) {
                    symbols.setNaN(decimalFormat.getNaN());
                    decimalFormatProperties = safePut(decimalFormatProperties, "NaN", decimalFormat.getNaN());
                }

                symbols.checkConsistency(formatStructuredName);
                decimalFormats = safePut(decimalFormats, formatName, decimalFormatProperties);
            }

            for (final Namespace namespace : testCaseEnvironment.getNamespaces()) {
                xqueryCompiler.declareNamespace(namespace.getPrefix(), namespace.getUri());
            }

            for (final FunctionLibrary functionLibrary : testCaseEnvironment.getFunctionLibraries()) {
                final String functionLibraryContent = readFileContent(path(testCaseEnvironment.getBaseUri(), functionLibrary.getXqueryLocation()));
                xqueryCompiler.compileLibrary(functionLibraryContent);
            }

            // Configure Collections
            for (final Collection collection : testCaseEnvironment.getCollections()) {

                    // create a collection URI resolved to handle the requested collections
                    URI collectionURI = collection.getUri();
                    if (collectionURI == null) {
                        collectionURI = new URI("");
                    }

                    // Iterate through the sources in the Collection
                    List<SourceInfo> collectionResources = null;
                    for (final Source source : collection.getSource()) {
                        collectionResources = safeAdd(collectionResources, new SourceInfo(source.getXmlId(), source.getUri(), path(testCaseEnvironment.getBaseUri(), source.getFile())));
//                        XdmNode doc = builder.build(file);
//                        sourceDocuments.put(id, doc);
                    }

                    // Register Collection
                    xqueryCompiler.getProcessor().getUnderlyingConfiguration().registerCollection(collectionURI.toString(), new FotsResourceCollection(collectionURI, documentBuilder, collectionResources));
            }

            // Configure Collations
            for (final Collation collation : testCaseEnvironment.getCollations()) {
                if ("http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind".equals(collation.getUri().toString())) {
                    final Configuration config = xqueryCompiler.getProcessor().getUnderlyingConfiguration();
                    final StringCollator collator = config.getCollationURIResolver().resolve("http://saxon.sf.net/collation?ignore-case=yes", config);

                    config.registerCollation(collation.getUri().toString(), collator);
                }
                if (collation.isDefault()) {
                    xqueryCompiler.declareDefaultCollation(collation.getUri().toString());
                }
            }
        }

//        return sourceDocuments, contextNode, variables, decimalFormats;
        return new DynamicContextInfo(sourceDocuments, contextItem, variables);
    }

    private static String getContent(final Test test) throws IOException {
        if (test.getContent() != null) {
            return test.getContent();
        }
        return readFileContent(test.getFile());
    }

    private Map<String, EnvironmentDefinition> getTestSetEnvironments(final TestSet testSet) throws ConnectorException {

        final AtomicReference<EnvironmentSetupState> testSetEnvironmentSetupState = testSetEnvironmentsSetupLocks.computeIfAbsent(testSet.getName(), key -> new AtomicReference<>(EnvironmentSetupState.UNINITIALIZED));

        // timeout settings for getting the text environments
        final long maxWait = 60 * 1024;     // 60 Seconds
        final long waitPerLoop = 200;       // 200 Milliseconds
        long waited = 0;

        while (true) {

            // check if we have exceeded a timeout
            if (waited > maxWait) {
                throw new ConnectorException(String.format("Max wait: %i exceeded when trying to get test set environments", maxWait));
            }

            // try and get existing environments for the test set
            if (testSetEnvironmentSetupState.get() == EnvironmentSetupState.READY) {
                final Map<String, EnvironmentDefinition> environments = testSetEnvironments.get(testSet.getName());
                if (environments == null) {
                    throw new IllegalStateException("Should not be possible to reach this state");
                }
            }

            // try and resolve the environments for the test set
            if (testSetEnvironmentSetupState.compareAndSet(EnvironmentSetupState.UNINITIALIZED, EnvironmentSetupState.CREATING)) {
                final Map<String, EnvironmentDefinition> environments = resolveTestSetEnvironments(testSet);
                testSetEnvironments.put(testSet.getName(), environments);
                testSetEnvironmentSetupState.set(EnvironmentSetupState.READY);
                return environments;
            }

            try {
                Thread.sleep(waitPerLoop);
            } catch (final InterruptedException e) {
                // restore thread's interrupted flag
                Thread.interrupted();
                throw new ConnectorException(String.format("Thread interrupted when trying to get test set environments: %s", e.getMessage()), e);
            }

            waited += waitPerLoop;
        }
    }

    private Map<String, EnvironmentDefinition> resolveTestSetEnvironments(final TestSet testSet) throws ConnectorException {
        final List<Environment> testSetEnvironments = testSet.getEnvironments();
        final Map<String, EnvironmentDefinition> results = new HashMap<>(testSetEnvironments.size());
        for (final Environment testSetEnvironment : testSetEnvironments) {
            final EnvironmentDefinition environment = resolveEnvironment(testSetEnvironment, globalEnvironments);
            results.put(environment.getName(), environment);
        }
        return Collections.unmodifiableMap(results);
    }

    private static EnvironmentDefinition resolveEnvironment(final Environment environment, final Map<String, EnvironmentDefinition>... environmentDefinitionss) throws ConnectorException {

        if (environment instanceof EnvironmentReference) {
            // de-reference
            EnvironmentDefinition result = null;
            for (final Map<String, EnvironmentDefinition> environmentDefinitions : environmentDefinitionss) {
                result = environmentDefinitions.get(environment.getName());
                if (result != null) {
                    return result;
                }
            }

            if (result == null) {
                throw new ConnectorException("Unable to resolve environment: " + environment.getName());
            }

            return result;

        } else if (environment instanceof final EnvironmentDefinition environmentDefinition) {
            return environmentDefinition;

        } else {
            throw new ConnectorException("Unknown environment type: " + environment.getClass().getName());
        }
    }

    private List<EnvironmentDefinition> getTestCaseEnvironments(final Map<String, EnvironmentDefinition> testSetEnvironments, final TestCase testCase) throws ConnectorException {
        final List<EnvironmentDefinition> results = new ArrayList<>(testSetEnvironments.size());
        for (final Environment testCaseEnvironment : testCase.getEnvironments()) {
            final EnvironmentDefinition environment = resolveEnvironment(testCaseEnvironment, globalEnvironments, testSetEnvironments);
            results.add(environment);
        }
        return Collections.unmodifiableList(results);
    }

    private static class NameAndType {
        final net.sf.saxon.s9api.QName name;
        @Nullable final String type;

        private NameAndType(final net.sf.saxon.s9api.QName name) {
            this(name, null);
        }

        private NameAndType(final net.sf.saxon.s9api.QName name, @Nullable final String type) {
            this.name = name;
            this.type = type;
        }
    }

    private static class SourceInfo {
        @Nullable final String id;
        @Nullable final URI uri;
        final Path file;

        private SourceInfo(@Nullable final String id, @Nullable final URI uri, final Path file) {
            this.id = id;
            this.uri = uri;
            this.file = file;
        }
    }

    private static class FotsResourceCollection implements ResourceCollection {

        private final URI collectionUri;
        private final DocumentBuilder documentBuilder;
        private @Nullable final List<SourceInfo> resources;

        // cache of resources content
        private @Nullable Map<Path, XmlResource> cachedResourcesContent;

        private FotsResourceCollection(final URI collectionUri, final DocumentBuilder documentBuilder, @Nullable final List<SourceInfo> resources) {
            this.collectionUri = collectionUri;
            this.documentBuilder = documentBuilder;
            this.resources = resources;
        }

        @Override
        public String getCollectionURI() {
            return collectionUri.toString();
        }

        @Override
        public Iterator<String> getResourceURIs(final XPathContext context) {
            if (resources == null) {
                return Collections.EMPTY_LIST.iterator();
            }

            return new ResourcesUriIterator();
        }

        @Override
        public Iterator<? extends net.sf.saxon.lib.Resource> getResources(final XPathContext context) {
            if (resources == null) {
                return Collections.EMPTY_LIST.iterator();
            }

            return new CachingResourcesIterator();
        }

        @Override
        public boolean isStable(final XPathContext context) {
            return true;
        }

        private abstract class AbstractResourcesIterator<T> implements Iterator<T> {
            private int offset = 0;

            @Override
            public boolean hasNext() {
                return offset < resources.size();
            }

            protected SourceInfo getNextResource() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return resources.get(offset++);
            }
        }

        private class CachingResourcesIterator extends ResourcesIterator {

            @Override
            public XmlResource next() {
                final SourceInfo next = getNextResource();

                // try and return from the cache
                XmlResource resource = null;
                if (cachedResourcesContent != null) {
                    resource = cachedResourcesContent.get(next.file);
                    if (resource != null) {
                        return resource;
                    }
                }

                // not in cache, so load it
                resource = load(next);

                // store it in the cache
                if (cachedResourcesContent == null) {
                    cachedResourcesContent = new HashMap<>();
                }
                cachedResourcesContent.put(next.file, resource);

                // return it
                return resource;
            }
        }

        private class ResourcesIterator extends AbstractResourcesIterator<XmlResource> {

            @Override
            public XmlResource next() {
                final SourceInfo next = getNextResource();
                return load(next);
            }

            protected XmlResource load(final SourceInfo sourceInfo) {
                try {
                    final XdmNode doc = documentBuilder.build(sourceInfo.file.toFile());
                    return new XmlResource(doc.getUnderlyingNode());
                } catch (final SaxonApiException e) {
                    throw new IllegalStateException(String.format("Unable to build resource: %s. Error: %s", sourceInfo.file, e.getMessage()), e);
                }
            }
        }

        private class ResourcesUriIterator extends AbstractResourcesIterator<String> {

            @Override
            public String next() {
                final SourceInfo next = getNextResource();
                if (next.uri != null) {
                    return next.uri.toString();
                }

                return collectionUri.resolve(next.file.toString()).toString();
            }
        }
    }

    private enum EnvironmentSetupState {
        UNINITIALIZED,
        CREATING,
        READY;
    }

    private static class DynamicContextInfo {
        @Nullable final Map<String, XdmNode> sourceDocuments;
        @Nullable final XdmItem contextItem;
        @Nullable final Map<net.sf.saxon.s9api.QName, XdmValue> variables;

        public DynamicContextInfo(@Nullable final Map<String, XdmNode> sourceDocuments, @Nullable final XdmItem contextItem, @Nullable final Map<net.sf.saxon.s9api.QName, XdmValue> variables) {
            this.sourceDocuments = sourceDocuments;
            this.contextItem = contextItem;
            this.variables = variables;
        }
    }

    private static class FotsUriResolver implements URIResolver {
        private @Nullable final Map<String, XdmNode> sourceDocuments;

        public FotsUriResolver(@Nullable final Map<String, XdmNode> sourceDocuments) {
            this.sourceDocuments = sourceDocuments;
        }

        @Override
        public javax.xml.transform.@Nullable Source resolve(final String href, final String base) {
            if (sourceDocuments != null) {
                final XdmNode node = sourceDocuments.get(href);
                if (node != null) {
                    return node.asSource();
                }
            }

            return null;
        }
    }

    private sealed interface EvaluationResult permits EvaluationResultFailure, EvaluationResultSuccess {
        static EvaluationResult success(final XdmValue value) {
            return new EvaluationResultSuccess(value);
        }

        static EvaluationResult failure(final SaxonApiException exception, @Nullable final List<ErrorInfo> errorsReported) {
            return new EvaluationResultFailure(exception, errorsReported);
        }
    }

    private static final class EvaluationResultSuccess implements EvaluationResult {
        private final XdmValue value;

        private EvaluationResultSuccess(final XdmValue value) {
            this.value = value;
        }
    }

    private static final class EvaluationResultFailure implements EvaluationResult {
        final SaxonApiException exception;
        @Nullable final List<ErrorInfo> errorsReported;

        private EvaluationResultFailure(final SaxonApiException exception, @Nullable final List<ErrorInfo> errorsReported) {
            this.exception = exception;
            this.errorsReported = errorsReported;
        }
    }

    private static class ErrorInfo {
        final net.sf.saxon.s9api.QName code;
        final String message;
        final int lineNumber;
        final int columnNumber;

        private ErrorInfo(final net.sf.saxon.s9api.QName code, final String message, final int lineNumber, final int columnNumber) {
            this.code = code;
            this.message = message;
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
        }
    }

    private static class FotsErrorReporter implements ErrorReporter {
        private @Nullable List<ErrorInfo> errors;

        @Override
        public void report(final XmlProcessingError error) {
            if (errors == null) {
                errors = new ArrayList<>();
            }

            errors.add(new ErrorInfo(
                error.getErrorCode(),
                error.getMessage(),
                error.getLocation().getLineNumber(),
                error.getLocation().getColumnNumber()
            ));
        }
    }
}
