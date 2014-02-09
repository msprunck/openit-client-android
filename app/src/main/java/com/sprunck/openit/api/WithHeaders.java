package com.sprunck.openit.api;

import java.util.Map;

/**
 * Implemented by Volley requests with the ability to add HTTP headers.
 *
 * @author Matthieu Sprunck
 */
interface WithHeaders {
    /**
     * Add HTTP headers to a Volley request.
     *
     * @param headers The headers to add
     */
    void addHeaders(Map<String, String> headers);
}
