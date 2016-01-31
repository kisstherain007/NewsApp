package com.ktr.newsapp.ui.simple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.ktr.ktrsupportlibrary.autoScrollViewPager.AutoScrollViewPager;
import com.ktr.ktrsupportlibrary.customViewPager.AutoHeightViewPager;
import com.ktr.ktrsupportlibrary.pagerAdapter.ImagePagerAdapter;
import com.ktr.ktrsupportlibrary.ui.BaseFragment;
import com.ktr.newsapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kisstherain on 2016/1/26.
 */
public class SimpleFragment extends BaseFragment {

    AutoHeightViewPager content_viewPager;
    AutoScrollViewPager autoScrollViewPager;

    public static SimpleFragment newInstance() {
        SimpleFragment fragment = new SimpleFragment();
        return fragment;
    }

    @Override
    protected int inflateContentView() {
        return R.layout.simple_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.view_pager);

        initRecomnd(autoScrollViewPager);

        content_viewPager = (AutoHeightViewPager) view.findViewById(R.id.content_viewPager);

        fragmentList.add(SimpleChildFragment.newInstance(5));
        fragmentList.add(SimpleChildFragment.newInstance(14));
        fragmentList.add(SimpleChildFragment.newInstance(10));

        content_viewPager.setAdapter(new SimplePagerAdapter(getFragmentManager()));
    }

    void initRecomnd(AutoScrollViewPager titleViewPager) {
        List<Integer> imageIdList = new ArrayList<Integer>();
        imageIdList.add(R.mipmap.banner3);
        imageIdList.add(R.mipmap.banner3);
        imageIdList.add(R.mipmap.banner3);
        imageIdList.add(R.mipmap.banner3);
        titleViewPager.setAdapter(new ImagePagerAdapter(getActivity(), imageIdList).setInfiniteLoop(true));
        titleViewPager.setInterval(2000);
        titleViewPager.startAutoScroll();
    }

    List<Fragment> fragmentList = new ArrayList<>();

    class SimplePagerAdapter extends FragmentPagerAdapter{

        public SimplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
