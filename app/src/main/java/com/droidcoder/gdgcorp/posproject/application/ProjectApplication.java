package com.droidcoder.gdgcorp.posproject.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import com.droidcoder.gdgcorp.posproject.dataentity.DaoMaster;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by DanLuciano on 1/9/2017.
 */

public class ProjectApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize greendao
        SQLiteDatabase db = DBHelper.getDb(this);
        DBHelper.getDaoMaster(db);
        DBHelper.getDaoSession();

        FirebaseDatabase.getInstance().setPersistenceEnabled(false);

    }

}
