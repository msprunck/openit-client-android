package com.sprunck.openit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sprunck.openit.ApplicationController;
import com.sprunck.openit.R;
import com.sprunck.openit.api.ApiError;
import com.sprunck.openit.api.ErrorListener;
import com.sprunck.openit.api.OpenItApi;
import com.sprunck.openit.api.SuccessListener;
import com.sprunck.openit.model.Command;

import javax.inject.Inject;
import java.util.List;

/**
 * A fragment representing a list of Commands.
 *
 * @author Matthieu Sprunck
 */
public class CommandFragment extends Fragment implements AbsListView.OnItemClickListener {
    /**
     * Log Tag.
     */
    private static final String TAG = CommandFragment.class.getSimpleName();

    /**
     * Fragment argument for device ID.
     */
    private static final String ARG_DEVICE_ID = "deviceId";

    /**
     * The selected device ID.
     */
    private String deviceId;

    /**
     * Listener notified when a command is clicked.
     */
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * To invoke OpenIt webservice.
     */
    @Inject
    OpenItApi openItApi;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<Command> mAdapter;

    /**
     * Create a command list fragment for the selected device.
     *
     * @param deviceId The selected device to load commands from.
     * @return a fragment with command list view
     */
    public static CommandFragment newInstance(String deviceId) {
        CommandFragment fragment = new CommandFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ApplicationController) getActivity().getApplication()).inject(this);

        if (getArguments() != null) {
            deviceId = getArguments().getString(ARG_DEVICE_ID);
        }

        // Display the command list
        final ActionBarActivity parent = (ActionBarActivity) getActivity();
        mAdapter = new ArrayAdapter<Command>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1);

        parent.setSupportProgressBarIndeterminateVisibility(true);

        // Get the commands from the OpenIt backend
        openItApi.getCommands(deviceId, new SuccessListener<List<Command>>() {
                    @Override
                    public void onSuccess(List<Command> result) {
                        for (Command command : result) {
                            mAdapter.add(command);
                        }
                        parent.setSupportProgressBarIndeterminateVisibility(false);
                    }
                }, new ErrorListener() {
                    @Override
                    public void onError(ApiError error) {
                        Log.e(TAG, "Unable to get the commands for the deviceId " + deviceId, error);
                        parent.setSupportProgressBarIndeterminateVisibility(false);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_command, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(deviceId, mAdapter.getItem(position));
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String deviceId, Command command);
    }

}
