package com.evolvedbinary.xth.parser.api;

public class ParserException extends Exception {

    public ParserException(final String message) {
        super(message);
    }

    public ParserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
