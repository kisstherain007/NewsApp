package com.ktr.newsapp.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ktr.ktrsupportlibrary.slidingTab.SlidingTabLayout;
import com.ktr.ktrsupportlibrary.ui.BaseFragment;
import com.ktr.newsapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kisstherain on 2016/1/10.
 */
public abstract class AbstractStripTabsFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    String[] titleArr;
//    List<Fragment> childFragments;
    Map<Integer, Fragment> fragments;

    SlidingTabLayout slidingTabs;
    ViewPager contentViewPager;
    MyViewPagerAdapter mViewPagerAdapter;

    Fragment mCurrentFragment;
    int mCurrentPosition = 0;
    int tabsCount = 0;

    abstract protected Fragment newFragment(int position);

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

    public void setTabsData(String[] titleArr, int tabsCount/*, List<Fragment> childFragments*/){

        this.tabsCount = tabsCount;

        fragments = new HashMap<Integer, Fragment>();

        this.titleArr = titleArr;

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

    class MyViewPagerAdapter extends CanRefreshFPagerAdapter {

        RecyclerView.RecycledViewPool mPool = new RecyclerView.RecycledViewPool(){

            @Override
            public void putRecycledView(RecyclerView.ViewHolder scrap) {
                super.putRecycledView(scrap);
            }

            @Override
            public RecyclerView.ViewHolder getRecycledView(int viewType) {
                return super.getRecycledView(viewType);
            }
        };

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);

            mPool.setMaxRecycledViews(0, 60);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = fragments.get(position);
            if (fragment == null) {
                fragment = newFragment(position);
                if (fragment instanceof ARecylclerReleaseFragment){
                    ((ARecylclerReleaseFragment) fragment).recycledViewPool = mPool;
                }
                fragments.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return tabsCount;
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
        return fragments.get(mCurrentPosition);
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
