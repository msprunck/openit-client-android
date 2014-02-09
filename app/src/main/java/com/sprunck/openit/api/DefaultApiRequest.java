package com.sprunck.openit.api;

import com.android.volley.Request;

/**
 * Volley api request wrapper.
 */
public class DefaultApiRequest implements ApiRequest {
    /**
     * Original Volley request.
     */
    private final Request<?> request;

    /**
     * Creates an API request from a Volley request.
     *
     * @param request a Volley request
     */
    public DefaultApiRequest(Request<?> request) {
        this.request = request;
    }

    @Override
    public final void cancel() {
        this.request.cancel();
    }
}
