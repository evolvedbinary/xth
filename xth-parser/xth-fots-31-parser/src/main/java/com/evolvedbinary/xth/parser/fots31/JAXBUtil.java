package com.evolvedbinary.xth.parser.fots31;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JAXBUtil {

    public static <T> T unmarshal(final Path schemaFile, final Class<T> classToBeBound, final Path xmlFile) throws IOException {
        try (final Reader catalogSchemaReader = Files.newBufferedReader(schemaFile, StandardCharsets.UTF_8);
                final Reader catalogReader = Files.newBufferedReader(xmlFile, StandardCharsets.UTF_8)) {

            final Schema catalogSchema = FOTS31Constants.SCHEMA_FACTORY.newSchema(new StreamSource(catalogSchemaReader));
            final Unmarshaller unmarshaller = createUnmarshaller(catalogSchema, classToBeBound);

            final JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(catalogReader), classToBeBound);

            return jaxbElement.getValue();

        } catch (final JAXBException | SAXException | IOException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private static <T> Unmarshaller createUnmarshaller(final Schema schema, final Class<T> classToBeBound) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(classToBeBound);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }
}
