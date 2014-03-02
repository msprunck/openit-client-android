package com.sprunck.openit.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.sprunck.openit.Endpoints;
import com.sprunck.openit.model.Command;
import com.sprunck.openit.model.Device;
import com.sprunck.openit.model.User;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Volley implementation for the OpenIt API.
 * See <a href="https://android.googlesource.com/platform/frameworks/volley">Volley GIT repository</a>
 *
 * @author Matthieu Sprunck
 */
public class VolleyOpenItApi implements OpenItApi {

    /**
     * Log or request TAG
     */
    private static final String TAG = VolleyOpenItApi.class.getSimpleName();

    /**
     * Global request queue for Volley
     */
    private final RequestQueue requestQueue;

    /**
     * To sign Volley request with a bearer token.
     */
    private final SignRequest signRequest;

    /**
     * The application context.
     */
    private final Context context;

    public VolleyOpenItApi(Context context) {
        this.context = context;

        // If you need to directly manipulate cookies later on, hold onto this client
        // object as it gives you access to the Cookie Store
        DefaultHttpClient httpClient = new DefaultHttpClient();

        CookieStore cookieStore = new BasicCookieStore();
        httpClient.setCookieStore(cookieStore);

        HttpStack httpStack = new HttpClientStack(httpClient);

        requestQueue = Volley.newRequestQueue(context, httpStack);
        signRequest = new SignRequest();
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    private RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req the request to add
     * @param tag tag of the request
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req the request to add
     */
    <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag tag of the requests to cancel
     */
    private void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    @Override
    public ApiRequest getDevices(final SuccessListener<List<Device>> success, final ErrorListener error) {
        final JsonArrayHeadersRequest req = new JsonArrayHeadersRequest(Endpoints.API_DEVICE_LIST, null, new DevicesListener(success), new DefaultErrorListener(error));
        signRequest.sign(req);
        //Cache.Entry cache = requestQueue.getCache().get(Endpoints.API_DEVICE_LIST);
        //Log.i(TAG, "CACHE: " + cache.ttl + ";" + (cache.isExpired() ? "expired": "valid"));

        addToRequestQueue(req);
        return new DefaultApiRequest(req);
    }

    @Override
    public ApiRequest getCommands(String deviceId, SuccessListener<List<Command>> success, ErrorListener error) {
        final JsonArrayHeadersRequest req = new JsonArrayHeadersRequest(Endpoints.API_COMMAND_LIST + '/' + deviceId + "/commands", null, new CommandsListener(success), new DefaultErrorListener(error));
        signRequest.sign(req);
        addToRequestQueue(req);
        return new DefaultApiRequest(req);
    }

    @Override
    public ApiRequest control(final String deviceId, final String command, final SuccessListener<JSONObject> success, final ErrorListener error) {
        final JsonObjectHeadersRequest req = new JsonObjectHeadersRequest(Request.Method.POST, Endpoints.API_CONTROL_COMMAND + '/' + deviceId + "/" + command, null, null, new DefaultListener<JSONObject>(success), new DefaultErrorListener(error));
        signRequest.sign(req);
        addToRequestQueue(req);
        return new DefaultApiRequest(req);
    }

    @Override
    public ApiRequest connect(final String accessToken, final SuccessListener<User> success, final ErrorListener error) {
        try {
            signRequest.setToken(accessToken);
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("access_token", accessToken);
            GsonRequest<User> req = new GsonRequest<User>(Request.Method.POST, Endpoints.API_CONNECT, jsonRequest,
                    User.class, null, new DefaultListener(success), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    GoogleAuthUtil.invalidateToken(context, accessToken);
                    error.onError(new ApiError(volleyError));
                }
            });
            getRequestQueue().getCache().clear();
            addToRequestQueue(req);
            return new DefaultApiRequest(req);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error.", e);
        }
        return null;
    }
}
