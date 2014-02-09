package com.sprunck.openit.api;

/**
 * Error thrown when a call to the OpenIt backend can not be done
 * successfully.
 */
public class ApiError extends Exception {
    /**
     * Creates an error from a message.
     *
     * @param message the error message
     */
    public ApiError(String message) {
        super(message);
    }

    /**
     * Creates an error from a message and a cause.
     *
     * @param message The error message.
     * @param cause   The cause
     */
    public ApiError(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an error from a cause.
     *
     * @param cause The cause
     */
    public ApiError(Throwable cause) {
        super(cause);
    }
}
