package com.sprunck.openit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.sprunck.openit.ApplicationController;
import com.sprunck.openit.R;
import com.sprunck.openit.api.ApiError;
import com.sprunck.openit.api.ErrorListener;
import com.sprunck.openit.api.OpenItApi;
import com.sprunck.openit.api.SuccessListener;
import com.sprunck.openit.model.Command;
import com.sprunck.openit.model.Device;
import com.sprunck.openit.session.Session;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity of the application.
 *
 * @author Matthieu Sprunck
 */
public class MainActivity extends ActionBarActivity implements CommandFragment.OnFragmentInteractionListener {
    /**
     * TAG for log.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Request code for voice recognition.
     */
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

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

        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        setSupportProgressBarIndeterminateVisibility(false);

        setupActionBar();

        /*
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         */
        session.checkLogin();

        // Create the spinner that contains available devices
        createSpinner();
    }

    /**
     * Setup actionbar.
     */
    private void setupActionBar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Logout menu
        if (id == R.id.action_logout) {
            session.logoutUser();
            return true;
        } else if (id == R.id.action_voice_recognition) {
            startVoiceRecognitionActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates the spinner containing available devices.
     */
    private void createSpinner() {
        final ArrayAdapter<Device> mSpinnerAdapter = new ArrayAdapter<Device>(this, android.R.layout.simple_spinner_dropdown_item);
        setSupportProgressBarIndeterminateVisibility(true);

        // Invoke the API to get devices
        openItApi.getDevices(new SuccessListener<List<Device>>() {
                                 @Override
                                 public void onSuccess(List<Device> devices) {
                                     for (Device device : devices) {
                                         mSpinnerAdapter.add(device);
                                     }
                                     setSupportProgressBarIndeterminateVisibility(false);
                                 }
                             }, new ErrorListener() {
                                 @Override
                                 public void onError(ApiError error) {
                                     Log.e(TAG, "Unable to get the devices when creating the spinner.", error);
                                     setSupportProgressBarIndeterminateVisibility(false);
                                 }
                             }
        );

        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                // Create new fragment from our own Fragment class
                CommandFragment newFragment = CommandFragment.newInstance(mSpinnerAdapter.getItem(position).id);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment container with this fragment
                //  and give the fragment a tag name equal to the string at the position selected
                ft.replace(R.id.container, newFragment);
                // Apply changes
                ft.commit();
                return true;
            }
        };
        getSupportActionBar().setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    @Override
    public void onFragmentInteraction(final String deviceId, final Command command) {
        // A command has been clicked. Send the command to the device through the site
        // using the OpenIt API
        openItApi.control(deviceId, command.name, new SuccessListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.i(TAG, "Command " + command + " successfully sent to device " + deviceId);
                        Toast.makeText(getApplication().getApplicationContext(), "Command sent", Toast.LENGTH_LONG).show();
                    }
                }, new ErrorListener() {
                    @Override
                    public void onError(ApiError error) {
                        Log.e(TAG, "Unable to send the command " + command + "for the deviceId " + deviceId, error);
                    }
                }
        );
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get recognition result strings
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
