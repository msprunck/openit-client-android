package com.sprunck.openit.api;

/**
 * OpenIt API error listener.
 *
 * @author Matthieu Sprunck
 */
public interface ErrorListener {
    /**
     * Invoked when an OpenIt API request failed.
     *
     * @param error the error
     */
    void onError(ApiError error);
}
