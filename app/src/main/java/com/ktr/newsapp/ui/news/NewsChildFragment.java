package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.bean.newsBean.NewsDetialPagebean;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;

/**
 * Created by kisstherain on 2015/11/16.
 */
public class NewsChildFragment extends AbstractNewsFragment {

    private static final String TAG = NewsChildFragment.class.getSimpleName();

    String mChannelId;

    public static NewsChildFragment newInstance(String channelId){
        NewsChildFragment fragment = new NewsChildFragment();
        fragment.mChannelId = channelId;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    String loadChannelId() {
        return mChannelId;
    }

    @Override
    void loadResult(NewsDetialPagebean newsDetialPagebean) {

        Log.i(TAG, newsDetialPagebean.getContentlist().size()+"");
    }

}
