package com.ktr.newsapp;

import android.app.Application;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;

import java.io.File;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsApp extends Application {

    private static NewsApp ourInstance = null;

    public static NewsApp getInstance() {

        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ourInstance = this;

        Fresco.initialize(ourInstance, FrescoConfig.getImagePipelineConfig(ourInstance));

        BitmapLoader.newInstance(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "kisstherainImage" + File.separator);
    }
}
