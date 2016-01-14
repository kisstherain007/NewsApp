package com.ktr.ktrsupportlibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ktr.ktrsupportlibrary.common.SupportLibraryApp;

/**
 * Created by n911305 on 2016/1/14.
 */
public class ScreenUtil {

    private static int screenWidth;

    private static int screenHeight;

    private static float density;

    private static void setScreenInfo() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) SupportLibraryApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;
    }

    public static int getScreenWidth() {
        if (screenWidth == 0)
            setScreenInfo();
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight == 0)
            setScreenInfo();
        return screenHeight;
    }

    public static float getDensity() {
        if (density == 0.0f)
            setScreenInfo();
        return density;
    }
}
