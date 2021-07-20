package com.aditya.myapplication2.activities.activity_package.image_encoder_and_decoder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class ImageBitmapString {
    //*******do not delete this file and do some research on it************//

    //this function handles image encoding
    // encoding image to String so that we can save the image to the room database
    @TypeConverter
    public static byte[] BitMapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        if (temp == null) {
            return null;
        } else
            return b;
    }

    //this function handles decoding of image
    //now here we will be decoding images that we will be getting from the room database to a bitmap format
    @TypeConverter
    public static Bitmap ByteArrayToBitMap(byte[] b) {
        try {
            byte[] encodeByte = b;
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            if (bitmap == null) {
                return null;
            } else {
                return bitmap;
            }

        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
