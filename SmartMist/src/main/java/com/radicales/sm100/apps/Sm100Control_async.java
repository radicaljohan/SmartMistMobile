package com.radicales.sm100.apps;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.radicales.sm100.device.Sm100;

/**
 * Created by Johan Russouw on 2015/02/14.
 */
public class Sm100Control_async extends AsyncTask<Sm100, Long, Integer> {

    public static final int ACTION_START = 0;
    public static final int ACTION_STOP = 1;
    public static final int ACTION_READ = 2;

    private Sm100 mSm100;
    private Integer mAction;
    // Handle to SharedPreferences for this app
    private SharedPreferences mPrefs;

    protected Integer doInBackground(Sm100... sm100) {
        if ( sm100.length > 0) {
            mSm100 = sm100[0];
            switch (mAction) {
                case ACTION_START:
                    connect();
                    break;
                case ACTION_STOP:
                    disconnect();
                    break;
                case ACTION_READ:
                    readprog();
                    break;
            }
        }

        return 0;
    }

    private void connect(){
        mSm100.start();
    }

    private void disconnect() {
     //   Toast.makeText(this,"Disconnecting", Toast.LENGTH_LONG).show();
        if(mSm100 != null) {
            mSm100.stop();
        }

    }
    private void readprog() {
      //  Toast.makeText(this,"Reading device", Toast.LENGTH_LONG).show();
        if(mSm100 != null) {
            mSm100.syncDownloadPrograms();
        }
    }

    public void setAction(Integer action) {
        mAction = action;
    }
    @Override
    protected void onPreExecute() {
    }
}