package com.droidcoder.gdgcorp.posproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

/**
 * Created by DanLuciano on 1/11/2017.
 */

public class ImageConverter {

    public static byte[] bitmapToBytes(Bitmap bitmap){
        byte[] bitmapBytes;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getResizedBitmap(bitmap, 150, 200).compress(Bitmap.CompressFormat.PNG, 100, stream);
        bitmapBytes = stream.toByteArray();

        return bitmapBytes;
    }

    public static Bitmap bytesToBitmap(byte[] bitmapBytes){

        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        //return getResizedBitmap(bitmap, 100, 100);
        return bitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {

        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;

    }

}
