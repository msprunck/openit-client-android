package com.sprunck.openit.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

/**
 * Volley Error listener that forward error to a given API error listener.
 */
class DefaultErrorListener implements Response.ErrorListener {
    /**
     * The API error listener to forward error.
     */
    private final ErrorListener listener;

    /**
     * Creates a Volley error listener with a provided API error
     * listener.
     *
     * @param listener an API error listener
     */
    public DefaultErrorListener(ErrorListener listener) {
        this.listener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError err) {
        listener.onError(new ApiError(err));
        VolleyLog.e("Error: ", err.getMessage());
    }
}
