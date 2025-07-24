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

import com.evolvedbinary.xth.parser.api.ParserConfiguration;
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

import static com.evolvedbinary.xth.parser.qt3tests.QT3TestsConstants.*;

public class QT3TestsParserProvider implements TestSuiteParserProvider {

    private static final QName CATALOG_ELEMENT_NAME = new QName("http://www.w3.org/2010/09/qt-fots-catalog", "catalog");
    private static final String TEST_SUITE_ATTRIBUTE_NAME = "test-suite";
    private static final String VERSION_ATTRIBUTE_NAME = "version";

    private static final AsyncXMLInputFactory ASYNC_XML_INPUT_FACTORY = new InputFactoryImpl();

    @Override
    public boolean canParse(final Path testSuiteDirectory) throws IOException {
        if (Files.isDirectory(testSuiteDirectory) && Files.isReadable(testSuiteDirectory)) {
            final Path catalogFile = testSuiteDirectory.resolve(CATALOG_FILE_NAME);
            if (Files.isRegularFile(catalogFile) && Files.isReadable(catalogFile)) {
                return isCatalogQT3Tests(catalogFile);
            }
        }
        return false;
    }

    private static boolean isCatalogQT3Tests(final Path catalogFile) throws IOException {
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
    public @Nullable TestSuiteParser newParser(final ParserConfiguration parserConfiguration, final Path testSuiteDirectory) throws IOException {
        if (!canParse(testSuiteDirectory)) {
            return null;
        }
        return new QT3TestsParser(parserConfiguration, testSuiteDirectory);
    }
}