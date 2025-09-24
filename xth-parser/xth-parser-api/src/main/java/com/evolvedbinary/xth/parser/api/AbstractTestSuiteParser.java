package com.evolvedbinary.xth.parser.api;

import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static com.evolvedbinary.xth.util.SetUtil.safeAdd;

public abstract class AbstractTestSuiteParser implements TestSuiteParser {

    protected @Nullable Set<ParserEventListener> listeners;

    @Override
    public void addEventListener(final ParserEventListener eventListener) {
        listeners = safeAdd(listeners, eventListener, LinkedHashSet::new);
    }

    @Override
    public void removeEventListener(final ParserEventListener eventListener) {
        if (listeners != null) {
            listeners.remove(eventListener);
        }
    }

    public void emitEvent(final Consumer<ParserEventListener> dispatch) {
        if (listeners != null) {
            final Iterator<ParserEventListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                final ParserEventListener eventListener = iterator.next();
                dispatch.accept(eventListener);
            }
        }
    }

    protected UUID generateUniqueId() {
        return UUID.randomUUID();
    }
}
