package com.sprunck.openit.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * POJO representing a device on OpenIt.
 *
 * @author Matthieu Sprunck
 */
public class Device {
    /**
     * The device name.
     */
    private final String name;

    /**
     * The device unique ID.
     */
    public final String id;

    /**
     * Creates a device (Ex: Raspberry Pi).
     *
     * @param name The device name
     * @param id   The device unique ID
     */
    private Device(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Creates a device from JSON object coming from the OpenIt backend.
     *
     * @param obj The JSON object with the device information
     * @return a command
     * @throws JSONException if the JSON object can not be parsed
     */
    public static Device fromJsonObject(JSONObject obj) throws JSONException {
        return new Device(obj.getString("name"), obj.getString("_id"));
    }

    @Override
    public String toString() {
        return name;
    }
}
