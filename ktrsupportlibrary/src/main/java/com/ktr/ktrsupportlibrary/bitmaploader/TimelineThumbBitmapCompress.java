package com.ktr.ktrsupportlibrary.bitmaploader;

import android.graphics.Bitmap;
import android.util.Log;

import com.ktr.ktrsupportlibrary.utils.BitmapUtils;

/**
 * Created by kisstherain on 2016/1/14.
 */
public class TimelineThumbBitmapCompress extends BitmapCompress {

    public static final String TAG = TimelineThumbBitmapCompress.class.getSimpleName();

    public static final int maxHeight = 1000;
    public static final int cutWidth = 550;
    public static final int cutHeight = 900;

    public Bitmap compress(byte[] bitmapBytes, ImageConfig config, int origW, int origH){

        Bitmap bitmap = null;

        float maxRadio = 6 * 1.0f / 16;

        if (origW * 1.0f / origH < maxRadio) {

            // 根据比例截取图片
            int width = origW;
            int height = width * (80 / 100);
//            int height = width * (timelineImageConfig.getShowHeight() / timelineImageConfig.getShowWidth());

            bitmap = BitmapUtils.decodeRegion(bitmapBytes, width, height);

            return bitmap;
        }

        // 高度比较高时，截图部分显示
        if (origW <= 440 && origH > maxHeight) {
            float outHeight = origW * 1.0f * (cutHeight * 1.0f / cutWidth);
            return BitmapUtils.decodeRegion(bitmapBytes, origW, Math.round(outHeight));
        }

        bitmap = super.compress(bitmapBytes, config, origW, origH);

        Log.d(TAG, String.format("原始尺寸是%dX%d, 压缩后尺寸是%dX%d", origW, origH, bitmap.getWidth(), bitmap.getHeight()));

        return bitmap;
    }
}
