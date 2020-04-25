package com.MTDap.commons.exception;

public class APIClientException extends Exception {

    public APIClientException() {
        super();
    }

    /**
     * Creates an client exception exception using given error message.
     *
     * @param message error message.
     */
    public APIClientException(String message) {
        super(message);
    }

    /**
     * Creates an client exception exception using a message and a inner cause.
     *
     * @param message error message.
     * @param cause   inner cause.
     */
    public APIClientException(String message, Throwable cause) {
        super(message, cause);
    }

}