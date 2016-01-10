package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktr.ktrsupportlibrary.autoScrollViewPager.AutoScrollViewPager;
import com.ktr.ktrsupportlibrary.pagerAdapter.ImagePagerAdapter;
import com.ktr.ktrsupportlibrary.slidingTab.SlidingTabLayout;
import com.ktr.newsapp.R;
import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ChannelList;
import com.ktr.newsapp.bean.newsBean.NewsChannelBean;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsFragment extends AbstractFragment {

    SlidingTabLayout slidingTabs;

    ViewPager contentViewPager;
    MyViewPagerAdapter mViewPagerAdapter;

    String[] titleArr;
    List<Fragment> childFragments = new ArrayList<>();

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slidingTabs = (SlidingTabLayout) view.findViewById(R.id.slidingTabs);
        contentViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager());
        slidingTabs.setCustomTabView(R.layout.comm_lay_tab_indicator, android.R.id.text1);
        slidingTabs.setSelectedIndicatorColors(getResources().getColor(R.color.maker_title_color));
        slidingTabs.setDistributeEvenly(false); //是否填充满屏幕的宽度

        executeLoadNewsChannel();
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = childFragments.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return childFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArr[position];
        }
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

                contentViewPager.setAdapter(mViewPagerAdapter);
                slidingTabs.setViewPager(contentViewPager);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
