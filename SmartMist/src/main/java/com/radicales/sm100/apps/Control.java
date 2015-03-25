package com.radicales.sm100.apps;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link Control.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Control#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Control extends Fragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Control.
     */
    // TODO: Rename and change types and number of parameters
    public static Control newInstance(String param1, String param2) {
        Control fragment = new Control();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Control() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        Button button = (Button) view.findViewById(R.id.button_connect);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                OnClick(v); /* my method to call new intent or activity */
            }
        });
        button = (Button) view.findViewById(R.id.button_disconnect);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                OnClick(v); /* my method to call new intent or activity */
            }
        });
        button = (Button) view.findViewById(R.id.button_proglist);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                OnClick(v); /* my method to call new intent or activity */
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
   //     mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.button_connect:
                Log.d("SmartMist", "Connect");
                if (mListener != null) {
                    mListener.onFragmentInteraction(0);
                }
                break;
            case R.id.button_disconnect:
                Log.d("SmartMist", "Disconnect");
                if (mListener != null) {
                    mListener.onFragmentInteraction(1);
                }
                break;
            case R.id.button_proglist:
                Log.d("SmartMist", "Get Programs");
                if (mListener != null) {
                    mListener.onFragmentInteraction(2);
                }
                break;
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(int Action);
    }

}
