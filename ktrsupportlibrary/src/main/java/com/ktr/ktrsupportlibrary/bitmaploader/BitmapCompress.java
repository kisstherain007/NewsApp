package com.ktr.ktrsupportlibrary.bitmaploader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by n911305 on 2016/1/14.
 */
public class BitmapCompress {

    public static final String TAG = BitmapCompress.class.getSimpleName();

    public Bitmap compress(byte[] bitmapBytes, ImageConfig config, int origW, int origH) {
        Bitmap bitmap = null;
        try {
            if (config.getMaxHeight() > 0 && config.getMaxWidth() > 0) {
                bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes, config.getMaxWidth(), config.getMaxHeight());
            } else if (config.getMaxHeight() > 0) {
                bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes, config.getMaxHeight(), config.getMaxHeight());
            } else if (config.getMaxWidth() > 0) {
                bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes, config.getMaxWidth(), config.getMaxWidth());
            } else {
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        Log.d(TAG, String.format("原始尺寸是%dX%d, 压缩后尺寸是%dX%d", origW, origH, bitmap.getWidth(), bitmap.getHeight()));

        return bitmap;
    }
}
