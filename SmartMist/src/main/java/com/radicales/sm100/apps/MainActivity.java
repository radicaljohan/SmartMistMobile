package com.radicales.sm100.apps;

import com.radicales.sm100.device.Sm100;
import com.radicales.sm100.device.Sm100Event;
import com.radicales.sm100.device.Sm100Program;
import com.radicales.sm100.device.Sm100Zone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends android.app.Activity implements  Sm100Event,Control.OnFragmentInteractionListener,
        ProgramlistFragment.OnProgramFragmentInteractionListener,
        ProgramDetailFragment.OnProgramDetailListener {

    private static String Tag = "MainActivity";

    private String mAppname;
    public Sm100 mSm100;

    private Handler mHandler;
    private String mState;
    private String mTime;
    private Menu mMenu;
    private ActionBar mActionBar;
    private TextView mTimeView;

    private Sm100Control_async sm100Control_async;
    public Integer mAction;
    /**
     * private property for mProgram names.
     */
    private String[] mProgNames;
    private String mProgName;
    private ProgramlistFragment mProgramListFragment;
    private ProgramDetailFragment mProgramFragment;

    private ProgressBar mProgress;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   final View controlsView = findViewById(R.id.fullscreen_content_controls);

        // Open Shared Preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mHandler = new Handler();


        mTimeView = (TextView)findViewById(R.id.textTime);
        mActionBar = getActionBar();
        mAppname = "Sm100 - ";
        mActionBar.setTitle(mAppname+"Offline");
        //setup UI
        SetupUI();

        //connect at startup
        connect();
    }

    private void SetupUI() {

        // Set up the action bar.
 /*       final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mProgress =  (ProgressBar) findViewById(R.id.progressBar);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
  //              actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
  /*              case 0:
                    return Control.newInstance("", "");
    */            case 0:
                    if (mProgNames == null) {
                        mProgNames = new String[0];

                        for (int i = 0; i < mProgNames.length; i++) {
                            mProgNames[i] = "No Programs Found";
                        }
                    }
                    mProgramListFragment = ProgramlistFragment.newInstance(mProgNames);
                    return mProgramListFragment;
                case 1:
                    mProgramFragment = ProgramDetailFragment.newInstance();
                    return mProgramFragment;
                default:
                    return PlaceholderFragment.newInstance(1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_program).toUpperCase(l);
                case 1:
                    return getString(R.string.title_zones).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mViewPager.getCurrentItem() == 1)
            {
                mViewPager.setCurrentItem(0);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void UpdateGUI(int id, String state) {
        switch (id){
            case 1:
                mState = state;
                mHandler.post(stateRunnable);
                break;
            case 2:
                mTime = state;
                mHandler.post(timeRunnable);
                break;
        }
    }

    final Runnable stateRunnable = new Runnable() {
        public void run() {
            Log.d("test",Tag);
            //      MenuItem i = mMenu.findItem(R.id.menu_status);
            mActionBar.setTitle(mAppname+mState);

            if(mState.contains("ed") || mState.contentEquals("Online") ) {
                mProgress.setIndeterminate(false);
            }
        }
    };
    final Runnable timeRunnable = new Runnable() {
        public void run() {
            mTimeView.setText(mTime);
        }
    };

    private void UpdateGUIFrame(int id) {
        switch (id){
            case 1:
                mHandler.post(frameRunnable);
                break;
        }
    }

    final Runnable frameRunnable = new Runnable() {
        public void run() {
            if (mProgramListFragment != null) {
                mProgramListFragment.setContent(mProgNames);

                mViewPager.setCurrentItem(0, true);
                mProgress.setIndeterminate(false);
            }
        }
    };

    private void UpdateProgram(String id) {
        mProgName = id;
        mHandler.post(frameDetailRunnable);
    }

    final Runnable frameDetailRunnable = new Runnable() {
        public void run() {
            if (mProgramFragment != null) {
                Sm100Program program = mSm100.findProgram(mProgName);
                mProgramFragment.setContent(program);

                mViewPager.setCurrentItem(2, true);
            }
        }
    };

    @Override
    public void onFragmentInteraction(int Action) {
        switch (Action){
            case 0:
                connect();
                break;
            case 1:
                disconnect();
                break;
            case 2:
                readprog();
                break;
        }
    }

    private void connect(){
        String ip = mPrefs.getString("ip", "192.168.0.202");
        String name = mPrefs.getString("dname","default");
        Integer port = mPrefs.getInt("dport",502);
        //      mSm100 = new Sm100("10.223.32.202", 502);
        if (mSm100 == null) {
            mSm100 = new Sm100(name, ip, port, true);
        }
        else {
            mSm100.setIpAddress(ip,port);
        }
        mSm100.registerEventListener(this);
        sm100Control_async = new Sm100Control_async();
        sm100Control_async.setAction(Sm100Control_async.ACTION_START);
        if ( mProgress != null) {
            mProgress.setIndeterminate(true);
        }
        sm100Control_async.execute(mSm100,null);
    }

    private void disconnect() {
        if(mSm100 != null) {
            sm100Control_async = new Sm100Control_async();
            sm100Control_async.setAction(Sm100Control_async.ACTION_STOP);
            if ( mProgress != null) {
                mProgress.setIndeterminate(true);
            }
            sm100Control_async.execute(mSm100,null);
        }
    }
    private void readprog() {
        if(mSm100 != null) {
            sm100Control_async = new Sm100Control_async();
            sm100Control_async.setAction(Sm100Control_async.ACTION_READ);
            if ( mProgress != null) {
                mProgress.setIndeterminate(true);
            }
            sm100Control_async.execute(mSm100,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                connect();
                return true;
            case R.id.menu_disconnect:
                disconnect();
                return true;
            case R.id.menu_refresh:
                readprog();
                return true;
            case R.id.menu_editdevice:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Log.d("SmartMist", "List OK");
            String progName = data.getStringExtra("ProgramName");
            if(progName != null) {
                mSm100.startProgram(progName);
            }
        }
        else {
            Log.d("SmartMist", "List Not OK");
        }
    }

    @Override
    public void eventProgramList(String[] Names) {
        Log.d("SmartMist", "eventProgramList");

    }

    @Override
    public void eventProgramConfig(Sm100Program Program) {

    }

    @Override
    public void eventProgramUpdate(List<Sm100Program> Programs) {
        Log.i("eventProgramUpdate",Tag);
        mProgNames = new String[Programs.size()];
        for(int i=0; i<mProgNames.length; i++) {
            mProgNames[i] = Programs.get(i).getName();
        }
        //TODO reload the program fragment
        //clear fragmant
        UpdateGUIFrame(1);
    }

    @Override
    public void eventZoneUpdate(List<Sm100Zone> Zones) {
        Log.i("eventZoneUpdate",Tag);
    }

    @Override
    public void eventStatus(String Message) {
        Log.i("eventStatus",Tag);
        //TODO fix with message
        UpdateGUI(1,Message);
    }
    @Override
    public void eventInformation( String Name, String Family, String Revision, int Channels ){}
    @Override
    public void eventDataTime( Date Time ){
        //Date/time from the device, let display it
        UpdateGUI(2,Time.toString());
    }

    @Override
    public void eventActiveProgram( boolean Active, Sm100Program Program, Sm100Zone Zone, String Status, int RunTime, int TimeToRun ){}
    @Override
    public void eventInputs( boolean[] Status ){}
    @Override
    public void eventUploadComplete(){}
    @Override
    public void eventZoneStatusUpdate( List<Sm100Zone> Zones ){}

    public void onProgramlistFragmentInteraction(String progName) {
        if(progName != null) {
            UpdateProgram(progName);
        }
    }

    public void OnProgramDetailFragmentInteraction (String prog) {
        //TODO implement action
        if (mSm100 != null) {
            mSm100.startProgram(prog);
        }
    }
}
