package com.example.sheldon.instagramclone.Util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by sheldon on 8/15/2017.
 */

public class ImageFinder {
    private HashMap<String, ArrayList<String>> directoryToImage;

    public ImageFinder() {
        directoryToImage = new HashMap<>();
    }
    public void getImages(Context context) {
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DEFAULT_SORT_ORDER};

        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        Log.d(TAG, "getImages: cursor size" + cursor.getCount());
        if(cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                String absPath = cursor.getString(image_path_col);
                Log.d(TAG, "getImages: absPath is " + absPath);
                int lastSeparator = absPath.lastIndexOf(File.separator);
                int nextLastSeparator = absPath.lastIndexOf(File.separator, lastSeparator - 1);

                String directoryName = absPath.substring(nextLastSeparator + 1, lastSeparator);
                Log.d(TAG, "getImages: directory name is " + directoryName);
                ArrayList<String> directory;
                if(directoryToImage.containsKey(directoryName)) {
                    Log.d(TAG, "getImages: accessing directory mapping for " + directoryName);
                    directory = directoryToImage.get(directoryName);
                    directory.add(absPath);
                }
                else {
                    Log.d(TAG, "getImages: adding directory mapping for " + directoryName);
                    directory = new ArrayList<>();
                    directory.add(absPath);
                    directoryToImage.put(directoryName, directory);
                }
            } while(cursor.moveToNext());
        }
    }

    public HashMap<String, ArrayList<String>> getImageMapping() {
        return directoryToImage;
    }
}
