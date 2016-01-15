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
import com.ktr.ktrsupportlibrary.slidingTab.SlidingTabLayout;
import com.ktr.ktrsupportlibrary.utils.Utility;
import com.ktr.newsapp.AppSetting;
import com.ktr.newsapp.R;
import com.ktr.newsapp.api.ApiManager;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.bean.newsBean.NewsDetailBean;
import com.ktr.newsapp.bean.newsBean.NewsDetialPagebean;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;
import com.ktr.newsapp.ui.newsDetail.NewsDetailActivity;
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
public abstract class AbstractStripTabsFragment extends AbstractFragment implements ViewPager.OnPageChangeListener{

    String[] titleArr;
    List<Fragment> childFragments;

    SlidingTabLayout slidingTabs;
    ViewPager contentViewPager;
    MyViewPagerAdapter mViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slidingTabs = (SlidingTabLayout) view.findViewById(R.id.slidingTabs);
        contentViewPager = (ViewPager) view.findViewById(R.id.pager);
    }

    public void setTabsData(String[] titleArr, List<Fragment> childFragments){

        this.titleArr = titleArr;
        this.childFragments = childFragments;

        mViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager());
        slidingTabs.setCustomTabView(R.layout.comm_lay_tab_indicator, android.R.id.text1);
        slidingTabs.setSelectedIndicatorColors(getResources().getColor(R.color.maker_title_color));
        slidingTabs.setDistributeEvenly(false); //是否填充满屏幕的宽度
        slidingTabs.setOnPageChangeListener(this);

        contentViewPager.setAdapter(mViewPagerAdapter);
        slidingTabs.setViewPager(contentViewPager);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
