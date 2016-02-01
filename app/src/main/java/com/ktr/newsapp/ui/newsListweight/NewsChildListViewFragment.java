package com.ktr.newsapp.ui.newsListweight;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ktr.ktrsupportlibrary.utils.Utility;
import com.ktr.newsapp.R;
import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.bean.newsBean.NewsDetailBean;
import com.ktr.newsapp.ui.news.AbstractStripTabsFragment;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kisstherain on 2015/11/16.
 */
public class NewsChildListViewFragment extends ARecylclerReleaseListViewFragment implements AbstractStripTabsFragment.IStripTabInitData {

    private static final String TAG = NewsChildListViewFragment.class.getSimpleName();

    String mChannelId;

    public static NewsChildListViewFragment newInstance(String channelId){
        NewsChildListViewFragment fragment = new NewsChildListViewFragment();
        fragment.mChannelId = channelId;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        if (getPagerCurrentFragment() == this){

            executeRequestData();
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getReleaseView().setRecyclerListener(this);

        Log.d(TAG, "onViewCreated");
    }

    List<ContentlistBean> contentlistBeans;

    public void executeSearchByChannelId(String channelId) {

        if (contentlistBeans != null) return;

        ApiManager.getDataByChannelId(channelId, 1).enqueue(new Callback<NewsDetailBean>() {
            @Override
            public void onResponse(Response<NewsDetailBean> response) {

                if (response.isSuccess()) {

                    contentlistBeans = response.body().getShowapi_res_body().getPagebean().getContentlist();

//                    kRecyclerAdapter.refreshAdapter(contentlistBeans);

                    initData(contentlistBeans);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    ListView getReleaseView() {
        return recyclerView;
    }

    @Override
    void executeRequestData() {

        Log.d(TAG, "executeRequestData()");

        if (Utility.notEmpty(mChannelId)) {

            executeSearchByChannelId(mChannelId);
        }
    }

    @Override
    protected int[] configCanReleaseIds() {
        return new int[]{R.id.content_img};
    }

    @Override
    public void onStripTabRequestData() {

       executeRequestData();
    }
}
