package com.ktr.newsapp.ui.news;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapOwner;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;

/**
 * Created by n911305 on 2016/1/15.
 */
public abstract class AReleaseFragment extends AbstractFragment implements BitmapOwner {

    public static final String TAG = AReleaseFragment.class.getSimpleName();

    public void releaseImageViewByIds() {

        Log.v(TAG, "releaseImageViewByIds()");

        if (getReleaseView() != null) {
            int childSize = getReleaseView().getChildCount();
            for (int i = 0; i < childSize; i++) {
                View view = getReleaseView().getChildAt(i);
                releaseImageView(view);
            }
        }
    }

    protected boolean releaseImageView(View container) {
        if (configCanReleaseIds() != null) {
            for (int imgId : configCanReleaseIds()) {
                ImageView imgView = (ImageView) container.findViewById(imgId);
                if (imgView != null) {
                    imgView.setImageDrawable(new ColorDrawable(Color.parseColor("#fff2f2f2")));
                    Log.v(AReleaseFragment.class.getSimpleName(), "释放ImageView");
                }
            }
        }

        return false;
    }


    Runnable releaseRunnable = new Runnable() {

        @Override
        public void run() {
            releaseImageViewByIds();
            Log.d(TAG, "释放图片");
        }

    };
    @Override
    public void onResume() {
        super.onResume();

        mHandler.post(releaseRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory.......");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView.......");
        if (BitmapLoader.getInstance() != null)
            BitmapLoader.getInstance().cancelPotentialTask(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach.......");
    }

    protected int[] configCanReleaseIds() {
        return null;
    }

    @Override
    public boolean canDisplay() {
        return true;
    }

    abstract RecyclerView getReleaseView();

    Handler mHandler = new Handler() {

    };
}
