package com.droidcoder.gdgcorp.posproject.utils;

import android.os.Handler;

/**
 * Created by DanLuciano on 12/28/2016.
 */

public class Delayer {

    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

}
