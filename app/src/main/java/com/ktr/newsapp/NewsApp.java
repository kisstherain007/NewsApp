package com.ktr.newsapp;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.common.SupportLibraryApp;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsApp extends SupportLibraryApp {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this, FrescoConfig.getImagePipelineConfig(this));

        BitmapLoader.newInstance(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "kisstherainImage" + File.separator);

        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        NewsApp application = (NewsApp) context.getApplicationContext();
        return application.refWatcher;
    }
}
