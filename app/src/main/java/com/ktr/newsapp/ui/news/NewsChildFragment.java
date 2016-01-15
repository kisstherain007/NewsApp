package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktr.ktrsupportlibrary.utils.Utility;
import com.ktr.newsapp.R;
import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.bean.newsBean.NewsDetailBean;
import com.ktr.newsapp.ui.newsDetail.NewsDetailActivity;
import com.ktr.newsapp.weight.DividerItemDecoration;
import com.ktr.newsapp.weight.KRecyclerAdapter;
import com.ktr.newsapp.weight.KRecyclerView;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kisstherain on 2015/11/16.
 */
public class NewsChildFragment extends AReleaseFragment {

    private static final String TAG = NewsChildFragment.class.getSimpleName();

    String mChannelId;

    public static NewsChildFragment newInstance(String channelId){
        NewsChildFragment fragment = new NewsChildFragment();
        fragment.mChannelId = channelId;
        return fragment;
    }

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

        kRecyclerAdapter.setmOnItemClickListener(new KRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object t) {

                NewsDetailActivity.startActivity(getActivity(), ((ContentlistBean) t).getLink());
            }
        });

        if (Utility.notEmpty(mChannelId)) {

            executeSearchByChannelId(mChannelId);
        }
    }

    public void executeSearchByChannelId(String channelId) {

        ApiManager.getDataByChannelId(channelId, 1).enqueue(new Callback<NewsDetailBean>() {
            @Override
            public void onResponse(Response<NewsDetailBean> response) {

                if (response.isSuccess()) {

                    kRecyclerAdapter.refreshAdapter(response.body().getShowapi_res_body().getPagebean().getContentlist());
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
    protected int[] configCanReleaseIds() {
        return new int[]{R.id.content_img};
    }
}
