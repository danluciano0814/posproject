package com.droidcoder.gdgcorp.posproject.datasystem;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by DanLuciano on 1/9/2017.
 */

public class CheckRegistration {

    static FileOutputStream fileOutputStream = null;
    static FileInputStream fileInputStream = null;

    public static void saveRegister(String value, Context context){

        try {
            fileOutputStream = context.openFileOutput("registration.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(value.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static boolean isRegistered(Context context) {

        int read;
        String value = "";
        try {
            fileInputStream = context.openFileInput("registration.txt");

            while((read = fileInputStream.read()) != -1){
                value += (char)read;
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return !value.equalsIgnoreCase("");
    }
}
