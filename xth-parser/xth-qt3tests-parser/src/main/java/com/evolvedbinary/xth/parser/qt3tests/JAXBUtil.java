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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JAXBUtil {

    public static <T> T unmarshal(final Path[] schemaFiles, final Class<T> classToBeBound, final Path xmlFile) throws IOException {
        final List<Reader> schemaReaders = new ArrayList<>(schemaFiles.length);

        try (final Reader catalogReader = Files.newBufferedReader(xmlFile, StandardCharsets.UTF_8)) {

            final List<Source> schemaSources = new ArrayList<>(schemaFiles.length);
            for (final Path schemaFile : schemaFiles) {
                final Reader schemaReader = Files.newBufferedReader(schemaFile, StandardCharsets.UTF_8);
                schemaReaders.add(schemaReader);
                schemaSources.add(new StreamSource(schemaReader));
            }

            final Schema catalogSchema = QT3TestsConstants.SCHEMA_FACTORY.newSchema(schemaSources.toArray(new Source[0]));
            final Unmarshaller unmarshaller = createUnmarshaller(catalogSchema, classToBeBound);

            final JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(catalogReader), classToBeBound);

            return jaxbElement.getValue();

        } catch (final JAXBException | SAXException | IOException e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            for (final Reader schemaReader : schemaReaders) {
                try {
                    schemaReader.close();
                } catch (final IOException e) {
                    // no-op
                }
            }

        }
    }

    private static <T> Unmarshaller createUnmarshaller(final Schema schema, final Class<T> classToBeBound) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(classToBeBound);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }
}
