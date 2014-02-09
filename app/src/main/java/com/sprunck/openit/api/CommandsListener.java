package com.sprunck.openit.api;

import android.util.Log;
import com.android.volley.Response;
import com.sprunck.openit.model.Command;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * To listen on successful response when requesting a list of commands for
 * a given device. Builds a list of device from the Json array.
 */
class CommandsListener implements Response.Listener<JSONArray> {
    /**
     * Tag for log.
     */
    private final static String TAG = CommandsListener.class.getSimpleName();

    /**
     * Listener called with the built list of commands.
     */
    private final SuccessListener<List<Command>> listener;

    /**
     * Creates a listener.
     *
     * @param success The application listener to notify after json parsing
     */
    public CommandsListener(SuccessListener<List<Command>> success) {
        this.listener = success;
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        List<Command> list = new ArrayList<Command>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(Command.fromJsonObject(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Json conversion error.", e);
            }
        }
        this.listener.onSuccess(list);
    }
}
