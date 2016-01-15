package com.ktr.ktrsupportlibrary.bitmaploader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;


import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;

import java.lang.ref.WeakReference;

public class CommonDrawable extends BitmapDrawable {

    private CommonBitmap myBitmap;
    private ImageConfig config;

    public CommonDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    public CommonDrawable(Resources res, CommonBitmap myBitmap, ImageConfig config) {
        this(res, myBitmap.getBitmap());
        this.myBitmap = myBitmap;
        this.config = config;
    }

    public ImageConfig getConfig() {
        return config;
    }

    public void setConfig(ImageConfig config) {
        this.config = config;
    }
}
