package com.example.sheldon.instagramclone.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 8/18/2017.
 */

public class ImageManager {
    public static Bitmap getBitMap(String imgURL) {
        File imgFile = new File(imgURL);
        FileInputStream fis = null;
        Bitmap bm = null;
        try {
            fis = new FileInputStream(imgFile);
            bm = BitmapFactory.decodeStream(fis);
        } catch(FileNotFoundException e) {
            Log.e(TAG, "getBitMap: FileNotFoundExeception", e);
        }
        finally {
            try {
                fis.close();
            }
            catch(IOException e) {
                Log.e(TAG, "getBitMap: IOException", e);
            }
        }
        return bm;
    }

    public static byte[] getBytesFromBitMap(Bitmap bm, int quality) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        return bos.toByteArray();
    }
}
