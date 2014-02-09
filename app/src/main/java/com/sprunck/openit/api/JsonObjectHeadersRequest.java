package com.sprunck.openit.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Volley JsonObjectRequest with HTTP headers.
 *
 * @author Matthieu Sprunck
 */
public class JsonObjectHeadersRequest extends JsonObjectRequest implements WithHeaders {
    /**
     * Map of request headers.
     */
    private final Map<String, String> headers;

    /**
     * Makes a JsonObjectRequest.
     *
     * @param method        the HTTP method to use
     * @param url           URL of the request to make
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null is allowed and
     *                      indicates no parameters will be posted along with request.
     * @param headers       Map of request headers
     * @param listener      Listener to receive the response
     * @param errorListener Listener to receive an error
     */
    public JsonObjectHeadersRequest(int method, String url, JSONObject jsonRequest, Map<String, String> headers, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public void addHeaders(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.putAll(headers);
    }
}
