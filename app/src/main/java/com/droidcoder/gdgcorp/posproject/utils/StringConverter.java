package com.droidcoder.gdgcorp.posproject.utils;

import java.text.DecimalFormat;

/**
 * Created by DanLuciano on 2/5/2017.
 */

public class StringConverter {

    public static String doubleFormatter(double value){
        return new DecimalFormat("#########0.00").format(value);
    }

}
