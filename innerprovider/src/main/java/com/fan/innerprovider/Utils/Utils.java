package com.fan.innerprovider.Utils;

import android.util.Log;

/**
 * Created by pc on 19-2-26.
 */

public class Utils {


    static final boolean DEBUG = true;
    static final boolean VERBOSE = true;
    static final String TAG = "inner_provider";

    public static void logD(String msg){
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void logV(String msg){
        if (VERBOSE) {
            Log.d(TAG, msg);
        }
    }

}
