package com.ktr.ktrsupportlibrary.common;

import android.app.Application;

/**
 * Created by n911305 on 2016/1/14.
 */
public class SupportLibraryApp extends Application {

    private static SupportLibraryApp ourInstance = null;

    public static SupportLibraryApp getInstance() {

        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ourInstance = this;
    }
}
