package com.ktr.ktrsupportlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

/**
 * Created by kisstherain on 2016/1/14.
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    public static Bitmap decodeRegion(byte[] bytes, int width, int height) {
        try {
            BitmapRegionDecoder bitmapDecoder = BitmapRegionDecoder.newInstance(bytes, 0, bytes.length, true);
            Rect rect = new Rect(0, 0, width, height);
            return bitmapDecoder.decodeRegion(rect, null).copy(Bitmap.Config.ARGB_8888, true);
        } catch (Exception e) {
        }
        return null;
    }
}
