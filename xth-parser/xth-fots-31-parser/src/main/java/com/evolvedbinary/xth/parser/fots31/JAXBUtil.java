package com.evolvedbinary.xth.parser.fots31;

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

            final Schema catalogSchema = FOTS31Constants.SCHEMA_FACTORY.newSchema(schemaSources.toArray(new Source[0]));
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
