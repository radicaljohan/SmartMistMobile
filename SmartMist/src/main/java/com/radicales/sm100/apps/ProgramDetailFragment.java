package com.radicales.sm100.apps;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.radicales.sm100.device.Sm100Program;
import com.radicales.sm100.device.StartTime;
import com.radicales.sm100.device.Sequence;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgramDetailFragment.OnProgramDetailListener} interface
 * to handle interaction events.
 * Use the {@link ProgramDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramDetailFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private Sm100Program mProgram;

    private OnProgramDetailListener mListener;
    private static List<String> mZoneList;
    private AbsListView mListView;
    private View mView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProgramDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramDetailFragment newInstance() {
        ProgramDetailFragment fragment = new ProgramDetailFragment();
        Bundle args = new Bundle();

        mZoneList = new ArrayList<String>();

        fragment.setArguments(args);
        return fragment;
    }

    public ProgramDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mZoneList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_program_detail, container, false);

        mListView = (AbsListView) mView.findViewById(R.id.listView);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        Button button = (Button) mView.findViewById(R.id.button_Run);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                OnClick(v); /* my method to call new intent or activity */
            }
        });

        return mView;
    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.button_Run:
                if (mListener != null) {
                    mListener.OnProgramDetailFragmentInteraction(mProgram.getName());
                }
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnProgramDetailListener) activity;
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

    public void setContent(Sm100Program program) {

        mProgram = program;
        View view = getView();
        TextView tvName = (TextView) view.findViewById(R.id.textName);
        TextView tvStart = (TextView) view.findViewById(R.id.textTime);
        tvName.setText(program.getName());
        StartTime[] starttimes = program.getStartTimes();
        tvStart.setText(String.valueOf(starttimes[0].toString()));



        mZoneList = new ArrayList<String>();
        for(Sequence s : program.getSequenceList()) {
            mZoneList.add(s.toString());
        }

        // Set the adapter
        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mZoneList);
        // Set the adapter
        mListView = (AbsListView) mView.findViewById(R.id.listView);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);


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
    public interface OnProgramDetailListener {
        // TODO: Update argument type and name
        public void OnProgramDetailFragmentInteraction(String prog);
    }

}
