package com.ktr.newsapp.ui.newsDetail;

import android.app.Activity;
import android.content.Intent;

import com.ktr.newsapp.ui.abstractui.AbstractAutoReleaseActivity;

/**
 * Created by kisstherain on 2016/1/31.
 */
public class NewsNativeDetailActivity extends AbstractAutoReleaseActivity {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, NewsNativeDetailActivity.class);
        activity.startActivity(intent);
    }
}
