package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ChannelList;
import com.ktr.newsapp.bean.newsBean.NewsChannelBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsFragment extends AutoReleaseFragment {

    String[] titleArr;
    List<Fragment> childFragments = new ArrayList<>();

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        executeLoadNewsChannel();
    }

    private void executeLoadNewsChannel(){

        ApiManager.getChannelData().enqueue(new Callback<NewsChannelBean>() {
            @Override
            public void onResponse(Response<NewsChannelBean> response) {

                List<ChannelList> channelLists = response.body().getShowapi_res_body().getChannelList();
                titleArr = new String[channelLists.size()];

                int i = 0;

                for (ChannelList channelList : channelLists){

                    titleArr[i++] = channelList.getName();
                    childFragments.add(NewsChildFragment.newInstance(channelList.getChannelId()));
                }


                setTabsData(titleArr, childFragments);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
