package com.droidcoder.gdgcorp.posproject.log;

import android.util.Log;

/**
 * Created by DanLuciano on 12/28/2016.
 */

public class AppLogger {
    public static final String LOG_NAME_TAG = "POSProject";

    public static void logE(String msg) {
        Log.e(LOG_NAME_TAG, msg);
    }

    public static void logD(String msg) {
        Log.d(LOG_NAME_TAG, msg);
    }

    public static void logI(String msg) {
        Log.i(LOG_NAME_TAG, msg);
    }

}
