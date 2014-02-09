package com.sprunck.openit.api;

import com.sprunck.openit.model.Command;
import com.sprunck.openit.model.Device;
import com.sprunck.openit.model.User;
import org.json.JSONObject;

import java.util.List;

/**
 * OpenIt API interface.
 *
 * @author Matthieu Sprunck
 */
public interface OpenItApi {
    /**
     * Creates a request to retrieve all devices for the current user.
     *
     * @param success the response listener
     * @param error   the error listener
     * @return an API request
     */
    ApiRequest getDevices(SuccessListener<List<Device>> success, ErrorListener error);

    /**
     * Creates a request to retrieve all commands of a device.
     *
     * @param deviceId a device ID
     * @param success  the response listener
     * @param error    the error listener
     * @return an API request
     */
    ApiRequest getCommands(String deviceId, SuccessListener<List<Command>> success, ErrorListener error);

    /**
     * Creates a request to connect a user with an authentication token.
     *
     * @param token   the authentication token (Google for now)
     * @param success the response listener
     * @param error   the error listener
     * @return an API request
     */
    ApiRequest connect(String token, SuccessListener<User> success, ErrorListener error);

    /**
     * Creates a request that send a command to the OpenIt device.
     *
     * @param deviceId The device ID to command
     * @param command  The command to send
     * @param success  the response listener
     * @param error    the error listener
     * @return an API request
     */
    ApiRequest control(String deviceId, String command, SuccessListener<JSONObject> success, ErrorListener error);
}
