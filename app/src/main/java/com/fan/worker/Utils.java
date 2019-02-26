package com.fan.worker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by FanFan on 19-2-26.
 */

public class Utils {

    public static final boolean VERBOSE = true;
    public static final boolean DEBUG = true;
    static final String TAG = "WorkerDemo";


    private static Toast sToast = null;

    static void showT(Context context, int resId) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        sToast.show();
    }

    static void showT(Context context, String msg) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        sToast.show();
    }

    static boolean isEmpty(String str) {
        return str == null || str.length() < 1;
    }

    static void logD(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    static void logV(String msg) {
        if (VERBOSE) {
            Log.v(TAG, msg);
        }
    }
}
