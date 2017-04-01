package com.droidcoder.gdgcorp.posproject.globals;

import android.net.ConnectivityManager;

/**
 * Created by DanLuciano on 1/11/2017.
 */

public class GlobalConstants {

    public static final String DB_NAME = "cheappos-db";

    //emails
    public static final String EMAIL_SENDER = "danluciano08@gmail.com";
    public static final String EMAIL_PASSWORD = "eizenn1008gaviel";

    //Sales settings
    public static final String TAX_FILE = "tax.txt";
    public static final String SC_FILE = "serviceCharge.txt";
    public  static  final String BUTTON_ONE_FILE = "btnOneFile.txt";
    public  static  final String BUTTON_TWO_FILE = "btnTwoFile.txt";
    public  static  final String BUTTON_THREE_FILE = "btnThreeFile.txt";

    public  static  final String PAYMENT_TYPE_CASH = "CASH";
    public  static  final String PAYMENT_TYPE_CREDIT = "CREDIT";
    public  static  final String PAYMENT_TYPE_POINTS = "POINTS";

    //Customer settings
    public static final String CUSTOMER_FEATURE = "customerFeature.txt";
    public static final String CUSTOMER_PURCHASE = "customerPurchase.txt";
    public static final String CUSTOMER_PURCHASE_POINTS = "customerPurchasePoints.txt";

    public static int[] getConnectivityTypes(){
        int[] connectivityTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        return connectivityTypes;
    }


}
