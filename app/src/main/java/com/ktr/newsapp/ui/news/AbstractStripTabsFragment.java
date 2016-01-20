package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
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

    Fragment mCurrentFragment;
    int mCurrentPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentPosition = 0;
    }

    @Override
    protected int inflateContentView() {
        return R.layout.news_fragment;
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
        contentViewPager.setOffscreenPageLimit(0);
        contentViewPager.setAdapter(mViewPagerAdapter);
        contentViewPager.setCurrentItem(mCurrentPosition);

        slidingTabs.setCustomTabView(R.layout.comm_lay_tab_indicator, android.R.id.text1);
        slidingTabs.setSelectedIndicatorColors(getResources().getColor(R.color.maker_title_color));
        slidingTabs.setDistributeEvenly(false); //是否填充满屏幕的宽度
        slidingTabs.setViewPager(contentViewPager);
        slidingTabs.setOnPageChangeListener(this);
        slidingTabs.setCurrent(mCurrentPosition);
    }

    class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = childFragments.get(position);

            mCurrentFragment = fragment;

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

    public Fragment getCurrentFragment() {

        if (mViewPagerAdapter == null || mViewPagerAdapter.getCount() < 0){

            return null;
        }
        return childFragments.get(mCurrentPosition);
    }

    @Override
    public void onPageSelected(int position) {

        mCurrentPosition = position;

        Fragment fragment = getCurrentFragment();
        if (fragment instanceof IStripTabInitData) {
            ((IStripTabInitData) fragment).onStripTabRequestData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // 这个接口用于多页面时，只有当前的页面才加载数据，其他不显示的页面暂缓加载
    // 当每次onPagerSelected的时候，再调用这个接口初始化数据
    public interface IStripTabInitData {

        public void onStripTabRequestData();

    }
}
