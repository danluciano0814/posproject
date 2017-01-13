package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DanLuciano on 1/11/2017.
 */

public class ConnectionHelper {

    public static boolean isNetworkAvailable(Context context, int[] networkTypes) {
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo netInfo = cm.getNetworkInfo(networkType);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    String command = "ping -c 1 google.com";
                    return (Runtime.getRuntime().exec (command).waitFor() == 0);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
