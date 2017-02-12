package com.droidcoder.gdgcorp.posproject;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.droidcoder.gdgcorp.posproject.log.AppLogger;

/**
 * Created by DanLuciano on 12/28/2016.
 */

public abstract class BaseCompatActivity extends AppCompatActivity {
    protected void logE(String msg) {
        AppLogger.logE(msg);
    }

    protected void logD(String msg) {
        AppLogger.logD(msg);
    }

    protected void logI(String msg) {
        AppLogger.logI(msg);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
