package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.droidcoder.gdgcorp.posproject.dataentity.DaoMaster;
import com.droidcoder.gdgcorp.posproject.dataentity.DaoSession;
import com.droidcoder.gdgcorp.posproject.globals.GlobalConstants;

/**
 * Created by DanLuciano on 1/9/2017.
 */

public class DBHelper {

    static DaoMaster daoMaster = null;
    static DaoSession daoSession = null;
    static SQLiteDatabase db = null;


    public static SQLiteDatabase getDb(Context context){

        if(db == null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, GlobalConstants.DB_NAME, null);
            db = helper.getWritableDatabase();
        }

        return db;
    }

    public static DaoMaster getDaoMaster(SQLiteDatabase db){

        if(daoMaster == null){
            daoMaster = new DaoMaster(db);
            Log.d("daoMaster", "dao master was initialize");
        }

        return daoMaster;
    }

    public static DaoSession getDaoSession(){

        if(daoSession == null){
            Log.d("daoSession", "dao master was initialize");
            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }

    public static void onUpdate(){

    }

}
