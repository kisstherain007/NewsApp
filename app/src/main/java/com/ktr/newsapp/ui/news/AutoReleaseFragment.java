package com.ktr.newsapp.ui.news;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.newsapp.ui.newsListweight.ARecylclerReleaseListViewFragment;

/**
 * Created by n911305 on 2016/1/15.
 */
public abstract class AutoReleaseFragment extends AbstractStripTabsFragment {

    int mCurrentPosition = 0;

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        Log.d("AutoReleaseFragment", "onPageSelected");
        mCurrentPosition = position;

        mHandler.removeCallbacks(releaseRunnable);
        mHandler.postDelayed(releaseRunnable, Math.round(2.0f * 1000));

        // 刷新当前显示
        mHandler.removeCallbacks(refreshRunnable);
        mHandler.postDelayed(refreshRunnable, 700);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(refreshRunnable);
        mHandler.removeCallbacks(releaseRunnable);
    }

    Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {

            BitmapLoader.getInstance().clearCache();
        }
    };

    Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {

            releaseFragment(mCurrentPosition - 1);
            releaseFragment(mCurrentPosition + 1);
        }
    };

    public void releaseFragment(int position){

        Log.d("ktr test" ,"releaseFragment ");

        if (position < fragments.size() && position >= 0) {

            Fragment fragment = fragments.get(position);

            if (fragment != null && fragment instanceof ARecylclerReleaseFragment){

                ((ARecylclerReleaseFragment) fragment).releaseImageViewByIds();
            }

            if (fragment != null && fragment instanceof ARecylclerReleaseListViewFragment){

                Log.d("ktr test" ,"release....");
                ((ARecylclerReleaseListViewFragment) fragment).releaseImageViewByIds();
            }
        }
    }

    Handler mHandler = new Handler() {};
}
