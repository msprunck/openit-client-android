package com.sprunck.openit.api;

import android.util.Log;
import com.android.volley.Response;
import com.sprunck.openit.model.Device;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A Volley listener that convert JSONArray to Device list and forward it
 * to an OpenIt API listener.
 */
class DevicesListener implements Response.Listener<JSONArray> {
    /**
     * Tag for Log.
     */
    private final static String TAG = DevicesListener.class.getSimpleName();

    /**
     * OpenIt API listener where the parsed result is forwarded.
     */
    private final SuccessListener<List<Device>> listener;

    /**
     * Creates a Volley listener with a given OpenIt API listener.
     *
     * @param listener an OpenIt API listener
     */
    public DevicesListener(SuccessListener<List<Device>> listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        List<Device> list = new ArrayList<Device>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(Device.fromJsonObject(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json conversion error.", e);
            }
        }
        this.listener.onSuccess(list);
    }
}
