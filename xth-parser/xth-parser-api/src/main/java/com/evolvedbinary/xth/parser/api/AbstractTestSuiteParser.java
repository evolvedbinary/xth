package com.evolvedbinary.xth.parser.api;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractTestSuiteParser implements TestSuiteParser {

    protected final Set<ParserEventListener> listeners = new LinkedHashSet<>();

    @Override
    public void addEventListener(final ParserEventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public void removeEventListener(final ParserEventListener eventListener) {
        listeners.remove(eventListener);
    }

    public void emitEvent(final Consumer<ParserEventListener> dispatch) {
        final Iterator<ParserEventListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            final ParserEventListener eventListener = iterator.next();
            dispatch.accept(eventListener);
        }
    }

    protected UUID generateUniqueId() {
        return UUID.randomUUID();
    }
}
