package com.ktr.newsapp.ui.news;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapOwner;
import com.ktr.ktrsupportlibrary.inject.ViewInject;
import com.ktr.newsapp.R;
import com.ktr.newsapp.ui.MainActivity;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;
import com.ktr.newsapp.weight.KRecyclerAdapter;
import com.ktr.newsapp.weight.KRecyclerView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by n911305 on 2016/1/15.
 */
public abstract class ARecylclerReleaseFragment extends AbstractFragment implements BitmapOwner, RecyclerView.RecyclerListener {

    public static final String TAG = ARecylclerReleaseFragment.class.getSimpleName();

    @ViewInject(idStr = "recyclerView")
    KRecyclerView recyclerView;
    KRecyclerAdapter kRecyclerAdapter;

    @Override
    protected int inflateContentView() {
        return R.layout.news_child_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        kRecyclerAdapter = new KRecyclerAdapter(this.getActivity(), this);
        recyclerView.setRecyclerListener(this);
        recyclerView.setAdapter(kRecyclerAdapter);
    }

    /**
     * 请求网络方法调用
     */
    abstract void executeRequestData();

    public Fragment getPagerCurrentFragment(){

        if (getActivity() == null ) return null;

        Fragment mainFragment = null;

        if (getActivity() instanceof MainActivity){

            mainFragment = ((MainActivity) getActivity()).homeFragmentManager.getFragmentByTag(MainActivity.MAIN_TAG);
        }

         if (mainFragment instanceof AbstractStripTabsFragment){

             AbstractStripTabsFragment abstractStripTabsFragment = (AbstractStripTabsFragment) mainFragment;

             return abstractStripTabsFragment.getCurrentFragment();
         }

        return null;
    }

    private Map<String, WeakReference<View>> viewCache = new HashMap<>();

    public void releaseImageViewByIds() {

        if (getReleaseView() != null) {
            int childSize = getReleaseView().getChildCount();
            for (int i = 0; i < childSize; i++) {
                View view = getReleaseView().getChildAt(i);
                releaseImageView(view);

                if (viewCache.containsKey(view.toString())) {
                    Log.v(TAG, "已经释放了，从Cache中移除");
                    viewCache.remove(view.toString());
                }
            }

            if (viewCache.size() > 0) {
                Set<String> keySet = viewCache.keySet();
                for (String key : keySet) {
                    View view = viewCache.get(key).get();
                    if (view != null) {
                        Log.v(TAG, "从Cache中释放一个View");

                        releaseImageView(view);
                    }
                }

                viewCache.clear();
            }
        }
    }

    protected boolean releaseImageView(View container) {
        if (configCanReleaseIds() != null) {
            for (int imgId : configCanReleaseIds()) {
                ImageView imgView = (ImageView) container.findViewById(imgId);
                if (imgView != null) {
                    imgView.setImageDrawable(new ColorDrawable(Color.parseColor("#fff2f2f2")));
                    Log.d(ARecylclerReleaseFragment.class.getSimpleName(), "释放ImageView");
                }
            }
        }

        return false;
    }

    Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {
            releaseImageViewByIds();
        }
    };

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

        if (!viewCache.containsKey(holder.itemView.toString())) {
            Log.d(TAG, holder.itemView.toString() + "保存一个View到Cache");
            viewCache.put(holder.itemView.toString(), new WeakReference<View>(holder.itemView));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        mHandler.postDelayed(releaseRunnable, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacks(releaseRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory.......");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach.......");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach.......");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView.......");
        if (BitmapLoader.getInstance() != null)
            BitmapLoader.getInstance().cancelPotentialTask(this);
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
