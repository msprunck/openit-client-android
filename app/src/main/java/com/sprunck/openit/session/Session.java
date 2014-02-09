package com.sprunck.openit.session;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.sprunck.openit.activity.LoginActivity;
import com.sprunck.openit.model.User;
import com.sprunck.openit.utils.Preferences;

/**
 * Manage the user session. Redirects to the login page if needed.
 *
 * @author Matthieu Sprunck
 */
public class Session {

    /**
     * The session related preferences name.
     */
    private final static String SESSION_PREFERENCES = "Session";

    /**
     * The key to retrieve the User.
     */
    private static final String KEY_PROFILE = "profile";

    /**
     * Value to check if there is no access token.
     */
    private static final String NO_PROFILE = "NO_PROFILE";

    /**
     * Preferences where session information are stored.
     */
    private final Preferences prefs;

    /**
     * The application context.
     */
    private final Context context;

    /**
     * Constructor.
     *
     * @param context The application context
     */
    public Session(Context context) {
        this.context = context;
        this.prefs = Preferences.getOrCreate(SESSION_PREFERENCES, context);
    }

    /**
     * Create login session
     */
    public void createLoginSession(User profile) {

        // Storing login value as TRUE
        Gson gson = new Gson();
        String json = gson.toJson(profile);

        prefs.set(KEY_PROFILE, json);

        // commit changes
        prefs.save();
    }

    /**
     * Get the logged user
     *
     * @return a user
     */
    public User getUser() {
        String json = prefs.get(KEY_PROFILE, "");
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isNotLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }

    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        this.prefs.clear();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Request logout
        i.putExtra(LoginActivity.LOGOUT, true);

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     * *
     */
    // Get Login State
    public boolean isNotLoggedIn() {
        return NO_PROFILE.equals(prefs.get(KEY_PROFILE, NO_PROFILE));
    }
}
