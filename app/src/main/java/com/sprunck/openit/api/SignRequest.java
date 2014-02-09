package com.sprunck.openit.api;

import java.util.HashMap;

/**
 * To sign a Volley request with a bearer token.
 *
 * @author Matthieu Sprunck
 */
class SignRequest {

    /**
     * Token to sign a given request.
     */
    private String token;

    /**
     * Sign a given request with the current token.
     *
     * @param request a request to sign
     */
    public void sign(WithHeaders request) {
        HashMap<String, String> additionalHeaders = new HashMap<String, String>();
        additionalHeaders.put("Authorization", "Bearer " + token);
        request.addHeaders(additionalHeaders);
    }

    /**
     * Updates the token used to sign requests.
     *
     * @param token a bearer token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
