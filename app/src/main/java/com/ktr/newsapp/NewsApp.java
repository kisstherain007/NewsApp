package com.ktr.newsapp;

import android.app.Application;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.common.SupportLibraryApp;

import java.io.File;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsApp extends SupportLibraryApp {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this, FrescoConfig.getImagePipelineConfig(this));

        BitmapLoader.newInstance(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "kisstherainImage" + File.separator);
    }
}
