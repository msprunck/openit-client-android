package com.sprunck.openit.api;

import com.android.volley.Response;

/**
 * A volley response listener that forward the successful response
 * to an OpenIt API {@link SuccessListener}.
 */
class DefaultListener<T> implements Response.Listener<T> {
    /**
     * The API listener to forward response.
     */
    private final SuccessListener<T> listener;

    /**
     * Creates a Volley listener for a given API {@link SuccessListener}.
     *
     * @param listener an OpenIt API listener
     */
    public DefaultListener(SuccessListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(T response) {
        this.listener.onSuccess(response);
    }
}
