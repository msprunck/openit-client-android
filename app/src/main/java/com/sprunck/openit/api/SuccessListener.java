package com.sprunck.openit.api;

/**
 * OpenIt API response listener when a response has been
 * handled successfully.
 *
 * @author Matthieu Sprunck
 */
public interface SuccessListener<T> {
    /**
     * Invoked when an OpenIt API request succeed.
     *
     * @param response the response from the OpenIt backend
     */
    void onSuccess(T response);
}
