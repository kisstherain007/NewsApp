package com.ktr.newsapp.ui.home.fragmentManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

import com.ktr.ktrsupportlibrary.fragmentmanager.AppFragmentCacheManager;
import com.ktr.newsapp.ui.news.NewsFragment;
import com.ktr.newsapp.ui.simple.SimpleFragment;


/**
 * Created by kisstherain on 2015/7/12.
 */
public class HomeFragmentManager extends AppFragmentCacheManager {

    public static final int news_tag = 0;

    public static final int simple_tag = 1;

    public HomeFragmentManager(FragmentActivity fragmentActivity, int layoutRes) {
        super(fragmentActivity, layoutRes);
    }

    @Override
    public void initFragments(SparseArray<Fragment> fragments, FragmentActivity mActivity) {

        fragments.append(news_tag, getNewsFragment());
        fragments.append(simple_tag, getSimpFragment());
    }

    public void changeFragmentByCache(int fragmentId) {
        super.changeFragmentByCache(fragmentId);
    }

    private Fragment getNewsFragment(){

        return NewsFragment.newInstance();
    }

    private Fragment getSimpFragment(){

        return SimpleFragment.newInstance();
    }
}
