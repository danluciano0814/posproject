package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.droidcoder.gdgcorp.posproject.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import static android.R.attr.width;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by DanLuciano on 1/11/2017.
 */

public class ImageConverter {

    public static byte[] bitmapToBytes(Bitmap bitmap){
        byte[] bitmapBytes;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getResizedBitmap(bitmap, 150, 150).compress(Bitmap.CompressFormat.PNG, 100, stream);
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

    public static Bitmap encodeAsBitmap(Context context, String str) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {

        }

        return bitmap;
    }

}
