package com.sprunck.openit;

import android.util.Log;

import java.util.Properties;

/**
 * The OpenIt API endpoints.
 *
 * @author Matthieu Sprunck
 */
public class Endpoints {
    private static final String TAG = Endpoints.class.getSimpleName();

    /**
     * The UserAgent string supplied with all PhotoHunt HTTP requests.
     */
    public static final String USER_AGENT = "OpenIt Agent";

    /**
     * The protocol and hostname used to access the OpenIt service.
     */
    private static final String API_HOST;

    /**
     * The URL root used to access the OpenIt API.
     */
    private static final String API_ROOT;

    /**
     * The API URL used to connect to the OpenIt service.
     */
    public static final String API_CONNECT;

    /**
     * The API URL used to disconnect from the OpenIt service.
     */
    public static final String API_DISCONNECT;

    /**
     * The API URL used to get the list of devices.
     */
    public static final String API_DEVICE_LIST;

    /**
     * The API URL used to get the list of command for a given device.
     */
    public static final String API_COMMAND_LIST;

    /**
     * The API URL used to control a command of a given device.
     */
    public static final String API_CONTROL_COMMAND;

    static {
        Properties config = new Properties();
        String apiHost = null;

        try {
            config.load(Endpoints.class.getClassLoader().getResourceAsStream("config.properties"));

            apiHost = config.getProperty("api_host");
        } catch (Exception e) {
            Log.e(TAG, "Failed to load configuration properties file", e);
        } finally {
            API_HOST = apiHost;

            if (API_HOST != null) {
                API_ROOT = API_HOST + "/api";
                API_CONNECT = API_ROOT + "/connect";
                API_DISCONNECT = API_ROOT + "/disconnect";
                API_DEVICE_LIST = API_ROOT + "/devices";
                API_COMMAND_LIST = API_ROOT + "/devices";
                API_CONTROL_COMMAND = API_ROOT + "/control";
            } else {
                API_ROOT = null;
                API_CONNECT = null;
                API_DISCONNECT = null;
                API_DEVICE_LIST = null;
                API_COMMAND_LIST = null;
                API_CONTROL_COMMAND = null;
            }
        }
    }
}
