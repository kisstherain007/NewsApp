package com.ktr.ktrsupportlibrary.bitmaploader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import com.ktr.ktrsupportlibrary.utils.ScreenUtil;

import java.io.IOException;

/**
 * Created by n911305 on 2016/1/14.
 */
public class BitmapProcess {

    public Bitmap compressBitmap(byte[] bitmapBytes, ImageConfig imageConfig){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        Bitmap bitmap = null;

        int maxWidth = imageConfig.getMaxWidth() == 0 ? ScreenUtil.getScreenWidth() : imageConfig.getMaxWidth();
        int maxHeight = imageConfig.getMaxHeight() == 0 ? ScreenUtil.getScreenHeight() : imageConfig.getMaxHeight();

        if (options.outWidth * 1.0f / options.outHeight > 2) {
            int reqHeight = maxHeight;

            // 截取局部图片
            BitmapRegionDecoder bitmapDecoder = null;
            try {

                bitmapDecoder = BitmapRegionDecoder.newInstance(bitmapBytes, 0, bitmapBytes.length, true);
                Rect rect = new Rect(0, 0, options.outWidth, reqHeight);
                bitmap = bitmapDecoder.decodeRegion(rect, null).copy(Bitmap.Config.ARGB_8888, true);
            } catch (IOException e) {
            }
        } else {
//            bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes, maxWidth, maxHeight);

            imageConfig.setMaxWidth(maxWidth);
            imageConfig.setMaxHeight(maxHeight);

            bitmap = new BitmapCompress().compress(bitmapBytes, imageConfig, options.outWidth, options.outHeight);
        }

        if (bitmap == null){

            bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes);
        }

        return bitmap;
    }
}
