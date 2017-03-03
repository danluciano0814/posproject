package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by DanLuciano on 2/28/2017.
 */

public class LFHelper {

    public static void saveLocalData(Context context, String fileName, String value){

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
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

    public static String getLocalData(Context context, String fileName){

        int read = -1;
        String value = "";
        FileInputStream fileInputStream;

        try {
            fileInputStream = context.openFileInput(fileName);

            while((read = fileInputStream.read()) != -1){
                value += (char)read;
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(value.equalsIgnoreCase("")){
            return "0";
        }

        return value;
    }

}
