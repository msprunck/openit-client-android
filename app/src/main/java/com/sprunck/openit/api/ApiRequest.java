package com.sprunck.openit.api;

/**
 * Cancellable request to the OpenIt backend.
 */
public interface ApiRequest {
    /**
     * Try to cancel the current request to the backend.
     */
    void cancel();
}
