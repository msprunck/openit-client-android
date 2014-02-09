package com.sprunck.openit.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Volley JsonArrayRequest with Http headers.
 */
public class JsonArrayHeadersRequest extends JsonArrayRequest implements WithHeaders {
    /**
     * Map of request headers.
     */
    private final Map<String, String> headers;

    /**
     * Makes a JsonArrayRequest.
     *
     * @param url           URL of the request to make
     * @param headers       Map of request headers
     * @param listener      Listener to receive the response
     * @param errorListener Listener to receive an error
     */
    public JsonArrayHeadersRequest(String url, Map<String, String> headers, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
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
