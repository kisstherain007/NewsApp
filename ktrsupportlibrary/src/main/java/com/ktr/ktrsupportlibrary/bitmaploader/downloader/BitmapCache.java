package com.ktr.ktrsupportlibrary.bitmaploader.downloader;

import android.util.Log;

import com.ktr.ktrsupportlibrary.bitmaploader.CommonBitmap;


public class BitmapCache {

    private static final String TAG = BitmapCache.class.getSimpleName();

    private LruMemoryCache<String, CommonBitmap> mMemoryCache;

    public BitmapCache(int memCacheSize) {
        init(memCacheSize);
    }

    private void init(int memCacheSize) {
        mMemoryCache = new LruMemoryCache<String, CommonBitmap>(memCacheSize) {
            @Override
            protected int sizeOf(String key, CommonBitmap bitmap) {
                return BitmapCommonUtils.getBitmapSize(bitmap.getBitmap()) * 4;
            }
        };
        mMemoryCache.size();
    }

    public void addBitmapToMemCache(String url, CommonBitmap bitmap) {
        if (url == null || bitmap == null) {
            return;
        }

        if (mMemoryCache != null) {
            mMemoryCache.put(url, bitmap);
            Log.i(TAG, "addBitmapToMemCache");
        }

    }

    public CommonBitmap getBitmapFromMemCache(String url) {

        Log.i(TAG, "getBitmapFromMemCache" + 1);

        if (mMemoryCache != null) {

            Log.i(TAG, "getBitmapFromMemCache" + 2 + url);

            final CommonBitmap memBitmap = mMemoryCache.get(url);

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

    public int getSize(){
        return mMemoryCache.size();
    }

    @Override
    public String toString() {
        return mMemoryCache.toString();
    }
}
