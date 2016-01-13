package com.ktr.ktrsupportlibrary.bitmaploader.downloader;

import android.graphics.Bitmap;
import android.util.Log;


public class BitmapCache {

    private static final String TAG = BitmapCache.class.getSimpleName();

    private LruMemoryCache<String, Bitmap> mMemoryCache;

    public BitmapCache(int memCacheSize) {
        init(memCacheSize);
    }

    private void init(int memCacheSize) {
        mMemoryCache = new LruMemoryCache<String, Bitmap>(memCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return BitmapCommonUtils.getBitmapSize(bitmap) * 4;
            }
        };

    }

    public void addBitmapToMemCache(String url, Bitmap bitmap) {
        if (url == null || bitmap == null) {
            return;
        }

        if (mMemoryCache != null) {
            mMemoryCache.put(url, bitmap);
            Log.i(TAG, "addBitmapToMemCache");
        }

    }

    public Bitmap getBitmapFromMemCache(String url) {

        Log.i(TAG, "getBitmapFromMemCache" + 1);

        if (mMemoryCache != null) {

            Log.i(TAG, "getBitmapFromMemCache" + 2 + url);

            final Bitmap memBitmap = mMemoryCache.get(url);

            if (memBitmap != null) {

                Log.i(TAG, "getBitmapFromMemCache" + 3);

                return memBitmap;
            }
        }
        return null;
    }

    public void clearMemCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }

    public void clearMemHalfCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictHalf();
        }
    }
}
