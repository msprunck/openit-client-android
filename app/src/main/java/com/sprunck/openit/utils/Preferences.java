package com.sprunck.openit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * To manage shared preferences easily.
 *
 * @author Matthieu Sprunck
 */
public class Preferences {
    /**
     * The corresponding shared preference.
     */
    private final SharedPreferences sharedPrefs;

    /**
     * Editor for Shared preferences.
     */
    private final SharedPreferences.Editor editor;

    /**
     * Log or request TAG
     */
    private static final String TAG = "Preferences";

    /**
     * Creates preferences in private mode.
     *
     * @param name    The corresponding shared preferences name
     * @param context The application context
     */
    private Preferences(String name, Context context) {
        sharedPrefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }

    /**
     * Gets or creates preferences with the provided name in private mode.
     *
     * @param name    The corresponding shared preferences name
     * @param context The application context
     * @return preferences
     */
    public static Preferences getOrCreate(String name, Context context) {
        return new Preferences(name, context);
    }

    /**
     * Clears all preferences.
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * Saves the preferences that have been set since the last record.
     */
    public void save() {
        editor.commit();
    }

    /**
     * Set a preference. It will be only took into account after {@link #save()} have been called.
     *
     * @param key   The preference key
     * @param value The preference value
     */
    public void set(String key, String value) {
        editor.putString(key, value);
    }

    /**
     * Get a preference from its key.
     *
     * @param key          The preference key to retrieve
     * @param defaultValue A default value if the key does not exists
     * @return the preference value or the default value if it does not exist.
     */
    public String get(String key, String defaultValue) {
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            Log.d(TAG, "Preference key " + key + " does not exist.", e);
            return defaultValue;
        }
    }
}