package com.evolvedbinary.xth.parser.fots31;

import com.evolvedbinary.xth.parser.api.TestSuiteParser;
import com.evolvedbinary.xth.parser.spi.TestSuiteParserProvider;
import com.fasterxml.aalto.AsyncByteBufferFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import org.jspecify.annotations.Nullable;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static com.evolvedbinary.xth.parser.fots31.FOTS31Constants.*;

public class FOTS31ParserProvider implements TestSuiteParserProvider {

    private static final QName CATALOG_ELEMENT_NAME = new QName("http://www.w3.org/2010/09/qt-fots-catalog", "catalog");
    private static final String TEST_SUITE_ATTRIBUTE_NAME = "test-suite";
    private static final String VERSION_ATTRIBUTE_NAME = "version";

    private static final AsyncXMLInputFactory ASYNC_XML_INPUT_FACTORY = new InputFactoryImpl();

    @Override
    public boolean canParse(final Path testSuiteDirectory) throws IOException {
        if (Files.isDirectory(testSuiteDirectory) && Files.isReadable(testSuiteDirectory)) {
            final Path catalogFile = testSuiteDirectory.resolve(CATALOG_FILE_NAME);
            if (Files.isRegularFile(catalogFile) && Files.isReadable(catalogFile)) {
                return isCatalogFots31(catalogFile);
            }
        }
        return false;
    }

    private static boolean isCatalogFots31(final Path catalogFile) throws IOException {
        boolean foundCatalogElement = false;
        boolean matchedTestSuiteName = false;
        boolean matchedTestSuiteVersion = false;

        int read = -1;
        final ByteBuffer buffer = ByteBuffer.allocate(256);

        try (final SeekableByteChannel channel = Files.newByteChannel(catalogFile, StandardOpenOption.READ)) {

            final AsyncXMLStreamReader<AsyncByteBufferFeeder> reader = ASYNC_XML_INPUT_FACTORY.createAsyncForByteBuffer();
            try {
                while (reader.hasNext()) {

                    switch (reader.next()) {
                        case AsyncXMLStreamReader.EVENT_INCOMPLETE:
                            // we need to send more input to the parser
                            read = channel.read(buffer);
                            if (read > -1) {
                                buffer.flip();
                                reader.getInputFeeder().feedInput(buffer);
                            } else {
                                reader.getInputFeeder().endOfInput();
                            }
                            break;

                        case XMLStreamConstants.START_ELEMENT:
                            final QName elementName = reader.getName();

                            // first element should be the `catalog` element
                            if (!foundCatalogElement && CATALOG_ELEMENT_NAME.equals(elementName)) {
                                foundCatalogElement = true;

                                // should contain `test-suite` and `version` attributes with appropriate values
                                for (int i = 0; i < reader.getAttributeCount(); i++) {
                                    final String attributeName = reader.getAttributeLocalName(i);

                                    if (!matchedTestSuiteName && TEST_SUITE_ATTRIBUTE_NAME.equals(attributeName)) {
                                        final String testSuiteName = reader.getAttributeValue(i);
                                        if (CATALOG_TEST_SUITE.equals(testSuiteName)) {
                                            matchedTestSuiteName = true;
                                        }
                                    }

                                    if (!matchedTestSuiteVersion && VERSION_ATTRIBUTE_NAME.equals(attributeName)) {
                                        final String testSuiteVersion = reader.getAttributeValue(i);
                                        if (CATALOG_VERSION.equals(testSuiteVersion)) {
                                            matchedTestSuiteVersion = true;
                                        }
                                    }
                                }

                                if (matchedTestSuiteName && matchedTestSuiteVersion) {
                                    return true;
                                }

                                break;
                            }

                            // encountered an element which was not the first element and not named `catalog`
                            return false;

                        case XMLStreamConstants.END_ELEMENT:
                            // first element should be the `catalog` element and data should have been present in the attributes
                            return false;
                    }
                }

            } finally {
                reader.close();
            }
        } catch (final XMLStreamException e) {
            throw new IOException(e.getMessage(), e);
        }

        return false;
    }

    @Override
    public @Nullable TestSuiteParser newParser(final Path testSuiteDirectory) throws IOException {
        if (!canParse(testSuiteDirectory)) {
            return null;
        }
        return new FOTS31Parser(testSuiteDirectory);
    }
}