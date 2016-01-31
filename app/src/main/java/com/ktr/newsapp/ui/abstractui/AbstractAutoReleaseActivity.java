package com.ktr.newsapp.ui.abstractui;

import android.os.Handler;
import android.util.Log;

import com.ktr.ktrsupportlibrary.ui.BaseActivity;

/**
 * Created by kisstherain on 2016/1/31.
 */
public abstract class AbstractAutoReleaseActivity extends BaseActivity {

    public static final String TAG = AbstractAutoReleaseActivity.class.getSimpleName();

    Handler mHandler = new Handler();

    @Override
    public void onPause() {
        super.onPause();

        mHandler.postDelayed(releaseRunnable, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(releaseRunnable);
    }

    Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {

            for (int i = 0; i < 10000; i++){

                Log.d(TAG, i+"");
            }
        }
    };
}
