package com.droidcoder.gdgcorp.posproject.globals;

import android.net.ConnectivityManager;

/**
 * Created by DanLuciano on 1/11/2017.
 */

public class GlobalConstants {

    public static final String DB_NAME = "cheappos-db";

    //emails server
    public static final String EMAIL_SENDER = "danluciano08@gmail.com";
    public static final String EMAIL_PASSWORD = "eizenn1008gaviel";

    //Store settngs
    public static final String STORE_LOGO_FILE = "storeLogo";
    public static final String STORE_NAME_FILE = "storeName";
    public static final String STORE_ADDRESS1_FILE = "address1";
    public static final String STORE_ADDRESS2_FILE = "address2";
    public static final String STORE_EMAIL_FILE = "storeEmail";
    public static final String STORE_PASSWORD_FILE = "storePassword";
    public static final String STORE_MOBILE_FILE = "storeMobile";
    public static final String STORE_LANDLINE_FILE = "storeLandline";

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
