package com.droidcoder.gdgcorp.posproject.globals;

import android.net.ConnectivityManager;

/**
 * Created by DanLuciano on 1/11/2017.
 */

public class GlobalConstants {

    public static final String DB_NAME = "cheappos-db";

    public static int[] getConnectivityTypes(){
        int[] connectivityTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        return connectivityTypes;
    }



}
