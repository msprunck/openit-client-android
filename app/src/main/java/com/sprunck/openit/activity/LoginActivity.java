package com.sprunck.openit.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.sprunck.openit.ApplicationController;
import com.sprunck.openit.R;
import com.sprunck.openit.api.ApiError;
import com.sprunck.openit.api.ErrorListener;
import com.sprunck.openit.api.OpenItApi;
import com.sprunck.openit.api.SuccessListener;
import com.sprunck.openit.auth.AuthUtil;
import com.sprunck.openit.model.User;
import com.sprunck.openit.session.Session;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Activity which displays a login screen to the user.
 * Only Google sign-in is currently available.
 *
 * @author Matthieu Sprunck
 */
public class LoginActivity extends FragmentActivity implements PlusClientFragment.OnSignInListener, View.OnClickListener {

    /**
     * Tag for log.
     */
    private static final String TAG = LoginActivity.class.getSimpleName();

    /**
     * Code used to identify the login request to the {@link PlusClientFragment}.
     */
    private static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;

    /**
     * LOGOUT extra information in the intent to request logout.
     */
    public static final String LOGOUT = "LOGOUT";

    /**
     * Delegate responsible for handling Google sign-in.
     */
    private PlusClientFragment mPlus;

    /**
     * Used to retrieve the OpenIt back end session id.
     */
    private AsyncTask<Object, Void, String> mAuthTask;

    /**
     * To invoke OpenIt webservice.
     */
    @Inject
    OpenItApi openItApi;

    /**
     * Handle login session.
     */
    @Inject
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ApplicationController) getApplication()).inject(this);

        setContentView(R.layout.activity_login);

        // Create the PlusClientFragment which will initiate authentication if
        // required.
        // AuthUtil.SCOPES describe the permissions that we are requesting of
        // the user to access
        // their information and write to their moments vault.
        // AuthUtil.ACTIONS describe the types of moment which we can
        // read from or write
        // to the user's vault.
        mPlus = PlusClientFragment.getPlusClientFragment(this, AuthUtil.SCOPES,
                AuthUtil.ACTIONS);

        /*
      Stores the @link com.google.android.gms.common.SignInButton} for use in
      the action bar.
     */
        SignInButton mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        // Delegate onActivityResult handling to PlusClientFragment to resolve
        // authentication
        // failures, eg. if the user must select an account or grant our
        // application permission to
        // access the information we have requested in AuthUtil.SCOPES and
        // AuthUtil.VISIBLE_ACTIVITIES
        mPlus.handleOnActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Reset any asynchronous tasks we have running.
        resetTaskState();
    }

    /**
     * Invoked when the {@link PlusClientFragment} delegate has successfully
     * authenticated the user.
     *
     * @param plusClient The connected PlusClient which gives us access to the Google+
     *                   APIs.
     */
    @Override
    public void onSignedIn(PlusClient plusClient) {
        if (plusClient.isConnected()) {
            // Retrieve the account name of the user which allows us to retrieve
            // the OAuth access
            // token that we securely pass over to the OpenIt service to
            // identify and
            // authenticate our user there.
            final String name = plusClient.getAccountName();

            // Asynchronously authenticate with the OpenIt service and
            // retrieve the associated
            // OpenIt profile for the user.
            mAuthTask = new AsyncTask<Object, Void, String>() {
                @Override
                protected String doInBackground(Object... o) {
                    try {
                        return GoogleAuthUtil.getToken(LoginActivity.this, name, AuthUtil.SCOPE_STRING);
                    } catch (IOException transientEx) {
                        // Network or server error, try later
                        Log.e(TAG, transientEx.toString(), transientEx);
                        return null;
                    } catch (GoogleAuthException authEx) {
                        // The call is not ever expected to succeed and should not be retried.
                        Log.e(TAG, authEx.toString(), authEx);
                        return null;
                    } catch (Exception e) {
                        Log.e(TAG, e.toString(), e);
                        throw new RuntimeException(e);
                    }
                }

                @Override
                protected void onPostExecute(String code) {
                    Log.d(TAG, "Authorization code retrieved:" + code);
                    if (code != null) {
                        apiConnect(code);
                    }
                }
            };

            mAuthTask.execute();
        }
    }

    /**
     * Connects the application to the OpenIt backend using
     * the webservice.
     *
     * @param token the google token
     */
    private void apiConnect(String token) {
        openItApi.connect(token, new SuccessListener<User>() {
                    @Override
                    public void onSuccess(User result) {
                        setAuthenticatedProfile(result);
                    }
                }, new ErrorListener() {
                    @Override
                    public void onError(ApiError error) {
                        setAuthenticatedProfile(null);
                        mPlus.signOut();
                        Toast toast = Toast.makeText(LoginActivity.this,
                                getString(R.string.connect_failed),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }
        );
    }

    /**
     * Invoked when the {@link PlusClientFragment} delegate has failed to
     * authenticate the user. Failure to authenticate will often mean that the
     * user has not yet chosen to sign in.
     * <p/>
     * The default implementation resets the OpenIt profile to null.
     */
    @Override
    public void onSignInFailed() {
        setAuthenticatedProfile(null);
    }

    /**
     * Invoked when the OpenIt profile has been successfully retrieved for an
     * authenticated user.
     *
     * @param profile the authenticated user
     */
    private void setAuthenticatedProfile(User profile) {
        if (profile != null) {
            session.createLoginSession(profile);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            mPlus.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
        }
    }

    /**
     * Resets the state of asynchronous tasks used by this activity.
     */
    private void resetTaskState() {
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
            mAuthTask = null;
        }
    }
}
