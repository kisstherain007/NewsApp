package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktr.ktrsupportlibrary.autoScrollViewPager.AutoScrollViewPager;
import com.ktr.ktrsupportlibrary.pagerAdapter.ImagePagerAdapter;
import com.ktr.ktrsupportlibrary.utils.Utility;
import com.ktr.newsapp.AppSetting;
import com.ktr.newsapp.R;
import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.bean.newsBean.NewsDetailBean;
import com.ktr.newsapp.bean.newsBean.NewsDetialPagebean;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;
import com.ktr.newsapp.weight.DividerItemDecoration;
import com.ktr.newsapp.weight.KRecyclerAdapter;
import com.ktr.newsapp.weight.KRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kisstherain on 2016/1/10.
 */
public abstract class AbstractNewsFragment extends AbstractFragment {

    KRecyclerView recyclerView;
    KRecyclerAdapter kRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_child_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (KRecyclerView) view.findViewById(R.id.recyclerView);
        kRecyclerAdapter = new KRecyclerAdapter(this.getActivity());
        recyclerView.setAdapter(kRecyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if (Utility.notEmpty(loadChannelId())) {

            executeSearchByChannelId(loadChannelId());
        }
    }

    public void executeSearchByChannelId(String channelId) {

        ApiManager.getDataByChannelId(channelId, 1).enqueue(new Callback<NewsDetailBean>() {
            @Override
            public void onResponse(Response<NewsDetailBean> response) {

                if (response.isSuccess()){

                    loadResult(response.body().getShowapi_res_body().getPagebean());

                    kRecyclerAdapter.refreshAdapter(response.body().getShowapi_res_body().getPagebean().getContentlist());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    abstract String loadChannelId();

    abstract void loadResult(NewsDetialPagebean newsDetialPagebean);
}
