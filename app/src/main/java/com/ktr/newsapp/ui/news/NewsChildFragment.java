package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ktr.ktrsupportlibrary.utils.Utility;
import com.ktr.newsapp.R;
import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.bean.newsBean.NewsDetailBean;
import com.ktr.newsapp.ui.newsDetail.NewsNativeDetailActivity;
import com.ktr.newsapp.weight.DividerItemDecoration;
import com.ktr.newsapp.weight.KRecyclerAdapter;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kisstherain on 2015/11/16.
 */
public class NewsChildFragment extends ARecylclerReleaseFragment implements AbstractStripTabsFragment.IStripTabInitData {

    private static final String TAG = NewsChildFragment.class.getSimpleName();

    String mChannelId;

    public static NewsChildFragment newInstance(String channelId){
        NewsChildFragment fragment = new NewsChildFragment();
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

        Log.d(TAG, "onViewCreated");
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        kRecyclerAdapter.setmOnItemClickListener(new KRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object t) {

//                NewsWebDetailActivity.startActivity(getActivity(), ((ContentlistBean) t).getLink());
                NewsNativeDetailActivity.launch(getActivity());
            }
        });
    }

    List<ContentlistBean> contentlistBeans;

    public void executeSearchByChannelId(String channelId) {

        if (contentlistBeans != null) return;

        ApiManager.getDataByChannelId(channelId, 1).enqueue(new Callback<NewsDetailBean>() {
            @Override
            public void onResponse(Response<NewsDetailBean> response) {

                if (response.isSuccess()) {

                    contentlistBeans = response.body().getShowapi_res_body().getPagebean().getContentlist();

                    kRecyclerAdapter.refreshAdapter(contentlistBeans);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    RecyclerView getReleaseView() {
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
