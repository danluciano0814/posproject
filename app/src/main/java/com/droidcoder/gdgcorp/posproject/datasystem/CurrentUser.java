package com.droidcoder.gdgcorp.posproject.datasystem;

import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.droidcoder.gdgcorp.posproject.dataentity.UserDao;
import com.droidcoder.gdgcorp.posproject.utils.DBHelper;


/**
 * Created by DanLuciano on 1/16/2017.
 */

public class CurrentUser {

    private static User user = null;

    public static void initUser(String password){

        user = DBHelper.getDaoSession().getUserDao().queryBuilder()
                .where(UserDao.Properties.PasswordCode.eq(password))
                .where(UserDao.Properties.Deleted.isNull()).list().get(0);

    }

    public static User getUser(){

        return user;

    }


}
