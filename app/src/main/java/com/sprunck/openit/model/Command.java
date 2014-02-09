package com.sprunck.openit.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * POJO representing a command from a device on OpenIt.
 *
 * @author Matthieu Sprunck
 */
public class Command {
    /**
     * The command name.
     */
    public final String name;

    /**
     * The text used by the voice recognition.
     */
    private final String voice;

    /**
     * Command unique ID.
     */
    private final String id;

    /**
     * Creates a command.
     *
     * @param name  the command name
     * @param id    the command unique ID
     * @param voice the text for the voice recognition
     */
    private Command(String name, String id, String voice) {
        this.id = id;
        this.name = name;
        this.voice = voice;
    }

    /**
     * Creates a command from JSON object coming from the OpenIt backend.
     *
     * @param obj The JSON object with the command information
     * @return a command
     * @throws JSONException if the JSON object can not be parsed
     */
    public static Command fromJsonObject(JSONObject obj) throws JSONException {
        return new Command(obj.getString("name"), obj.getString("_id"), obj.getString("voice"));
    }

    @Override
    public String toString() {
        return name;
    }
}
