package com.ktr.ktrsupportlibrary.bitmaploader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ktr.ktrsupportlibrary.common.SupportLibraryApp;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kisstherain on 2016/1/15.
 */
public class CommonBitmap {

    public static final String TAG = CommonBitmap.class.getSimpleName();

    static int createdCount = 0;

    private Bitmap bitmap;

    private String url;

    private static Map<String, WeakReference<Bitmap>> cacheMap;

    private CommonBitmap() {
        createdCount++;
        Log.v(TAG, createdCount + "");
    }

    public CommonBitmap(int resId) {
        this();
        this.bitmap = getCacheBitmap(resId);
    }

    public CommonBitmap(int resId, String url) {
        this();
        this.bitmap = getCacheBitmap(resId);
        this.url = url;
    }

    public CommonBitmap(Bitmap bitmap, String url) {
        this.url = url;
        this.bitmap = bitmap;
    }

    static Bitmap getCacheBitmap(int resId) {
        String key = String.valueOf(resId);
        Bitmap bitmap = null;

        if (cacheMap.containsKey(key)) {
            bitmap = cacheMap.get(key).get();
        }

        if (bitmap == null) {
            try {
                bitmap = BitmapFactory.decodeResource(SupportLibraryApp.getInstance().getResources(), resId);
                cacheMap.put(key, new WeakReference<Bitmap>(bitmap));
            } catch (Error e) {
                return Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_4444);
            }
        }

        return bitmap;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        createdCount--;
        Log.v(TAG, createdCount + "");
    }

    static {
        cacheMap = new HashMap<String, WeakReference<Bitmap>>();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
