package com.evolvedbinary.xth.connector.api;

public class ConnectorException extends Exception {

    public ConnectorException(final String message) {
        super(message);
    }

    public ConnectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
