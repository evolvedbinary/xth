package com.evolvedbinary.xth.reporting.otr;

import com.evolvedbinary.xth.reporting.api.TestResultsListener;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import com.evolvedbinary.xth.tsom.result.TestCaseResult;
import com.evolvedbinary.xth.tsom.result.TestCaseResultError;
import com.evolvedbinary.xth.tsom.result.TestCaseResultFailure;
import com.evolvedbinary.xth.tsom.result.TestCaseResultPass;
import com.evolvedbinary.xth.tsom.result.TestCaseResultSkipped;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TestResultsListenerJUnitXml implements TestResultsListener {

    private static final String NS_CORE = "https://schemas.opentest4j.org/reporting/core/0.2.0";
    private static final String NS_EVENTS = "https://schemas.opentest4j.org/reporting/events/0.2.0";

    private final Path outputDirectory;
    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final ConcurrentMap<String, Integer> testSetIds = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> testSetWorstStatus = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> testCaseIds = new ConcurrentHashMap<>();
    private final List<OtrEvent> events = new CopyOnWriteArrayList<>();

    public TestResultsListenerJUnitXml(final Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void testSetStarted(final TestSet testSet, final Instant timestamp) {
        final int id = testSetIds.computeIfAbsent(testSet.getName(), k -> idCounter.incrementAndGet());
        events.add(new StartedEvent(id, testSet.getName(), null, timestamp));
    }

    @Override
    public void testSetFinished(final TestSet testSet, final Instant timestamp) {
        final int id = testSetIds.computeIfAbsent(testSet.getName(), k -> idCounter.incrementAndGet());
        final String status = testSetWorstStatus.getOrDefault(testSet.getName(), "SUCCESSFUL");
        events.add(new FinishedEvent(id, status, timestamp));
    }

    @Override
    public void testCaseStarted(final TestSet testSet, final TestCase testCase, final Instant timestamp) {
        final int parentId = testSetIds.computeIfAbsent(testSet.getName(), k -> idCounter.incrementAndGet());
        final int id = idCounter.incrementAndGet();
        testCaseIds.put(testCaseKey(testSet, testCase), id);
        events.add(new StartedEvent(id, testCase.getName(), parentId, timestamp));
    }

    @Override
    public void testCaseFinished(final TestSet testSet, final TestCase testCase, final Instant timestamp, final TestCaseResult testCaseResult) {
        final String status = toStatus(testCaseResult);
        testSetWorstStatus.merge(testSet.getName(), status, TestResultsListenerJUnitXml::worstStatus);
        final Integer id = testCaseIds.get(testCaseKey(testSet, testCase));
        if (id != null) {
            events.add(new FinishedEvent(id, status, timestamp));
        }
    }

    @Override
    public void close() throws IOException {
        Files.createDirectories(outputDirectory);
        final Path file = outputDirectory.resolve("xth-test-results.xml");
        final XMLOutputFactory factory = XMLOutputFactory.newFactory();
        try (final OutputStream out = Files.newOutputStream(file)) {
            XMLStreamWriter writer = null;
            try {
                writer = factory.createXMLStreamWriter(out, "UTF-8");
                writeDocument(writer);
            } catch (final XMLStreamException e) {
                throw new IOException("Failed to write OTR test results report", e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (final XMLStreamException e) {
                        throw new IOException("Failed to close XML writer", e);
                    }
                }
            }
        }
    }

    private void writeDocument(final XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeCharacters("\n");

        writer.writeStartElement("e", "events", NS_EVENTS);
        writer.writeDefaultNamespace(NS_CORE);
        writer.writeNamespace("e", NS_EVENTS);
        writer.writeCharacters("\n    ");

        writer.writeStartElement(NS_CORE, "infrastructure");
        writer.writeCharacters("\n        ");
        writer.writeStartElement(NS_CORE, "hostName");
        writer.writeCharacters(hostName());
        writer.writeEndElement();
        writer.writeCharacters("\n        ");
        writer.writeStartElement(NS_CORE, "userName");
        writer.writeCharacters(System.getProperty("user.name", "unknown"));
        writer.writeEndElement();
        writer.writeCharacters("\n    ");
        writer.writeEndElement(); // infrastructure
        writer.writeCharacters("\n");

        final List<OtrEvent> sorted = events.stream()
                .sorted(Comparator.comparing(OtrEvent::timestamp))
                .toList();

        for (final OtrEvent event : sorted) {
            switch (event) {
                case StartedEvent s -> writeStarted(writer, s);
                case FinishedEvent f -> writeFinished(writer, f);
            }
        }

        writer.writeEndElement(); // e:events
        writer.writeEndDocument();
        writer.flush();
    }

    private static void writeStarted(final XMLStreamWriter writer, final StartedEvent event) throws XMLStreamException {
        writer.writeCharacters("    ");
        writer.writeEmptyElement("e", "started", NS_EVENTS);
        writer.writeAttribute("id", String.valueOf(event.id()));
        writer.writeAttribute("name", event.name());
        if (event.parentId() != null) {
            writer.writeAttribute("parentId", String.valueOf(event.parentId()));
        }
        writer.writeAttribute("time", event.timestamp().toString());
        writer.writeCharacters("\n");
    }

    private static void writeFinished(final XMLStreamWriter writer, final FinishedEvent event) throws XMLStreamException {
        writer.writeCharacters("    ");
        writer.writeStartElement("e", "finished", NS_EVENTS);
        writer.writeAttribute("id", String.valueOf(event.id()));
        writer.writeAttribute("time", event.timestamp().toString());
        writer.writeCharacters("\n        ");
        writer.writeEmptyElement("result");
        writer.writeAttribute("status", event.status());
        writer.writeCharacters("\n    ");
        writer.writeEndElement(); // e:finished
        writer.writeCharacters("\n");
    }

    private static String toStatus(final TestCaseResult result) {
        return switch (result) {
            case TestCaseResultPass _ -> "SUCCESSFUL";
            case TestCaseResultFailure _ -> "FAILED";
            case TestCaseResultError _ -> "ABORTED";
            case TestCaseResultSkipped _ -> "SKIPPED";
        };
    }

    private static String worstStatus(final String a, final String b) {
        return statusPriority(a) >= statusPriority(b) ? a : b;
    }

    private static int statusPriority(final String status) {
        return switch (status) {
            case "ABORTED" -> 3;
            case "FAILED" -> 2;
            case "SKIPPED" -> 1;
            default -> 0;
        };
    }

    private static String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            return "unknown";
        }
    }

    private static String testCaseKey(final TestSet testSet, final TestCase testCase) {
        return testSet.getName() + ' ' + testCase.getName();
    }

    private sealed interface OtrEvent permits StartedEvent, FinishedEvent {
        Instant timestamp();
    }

    private record StartedEvent(int id, String name, Integer parentId, Instant timestamp) implements OtrEvent {}

    private record FinishedEvent(int id, String status, Instant timestamp) implements OtrEvent {}
}