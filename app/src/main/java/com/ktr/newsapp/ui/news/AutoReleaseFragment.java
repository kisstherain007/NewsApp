package com.ktr.newsapp.ui.news;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;

/**
 * Created by n911305 on 2016/1/15.
 */
public abstract class AutoReleaseFragment extends AbstractStripTabsFragment {

    int mCurrentPosition = 0;

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);

        mCurrentPosition = position;
        mHandler.post(releaseRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.post(releaseRunnable);
    }

    Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {

            releaseFragment(mCurrentPosition - 1);
            releaseFragment(mCurrentPosition + 1);

            BitmapLoader.getInstance().clearCache();
        }
    };

    public void releaseFragment(int position){

        if (position < childFragments.size() && position >= 0) {

            Fragment fragment = childFragments.get(position);

            if (fragment != null && fragment instanceof AReleaseFragment){

                ((AReleaseFragment)fragment).releaseImageViewByIds();
            }
        }
    }

    Handler mHandler = new Handler() {};
}
