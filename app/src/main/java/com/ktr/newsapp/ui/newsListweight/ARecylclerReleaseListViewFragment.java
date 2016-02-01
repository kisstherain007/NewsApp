package com.ktr.newsapp.ui.newsListweight;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ktr.ktrsupportlibrary.adapter.BaseAdapterHelper;
import com.ktr.ktrsupportlibrary.adapter.QuickAdapter;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;
import com.ktr.ktrsupportlibrary.inject.ViewInject;
import com.ktr.ktrsupportlibrary.ui.BaseFragment;
import com.ktr.newsapp.R;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.ui.MainActivity;
import com.ktr.newsapp.ui.news.AbstractStripTabsFragment;
import com.ktr.newsapp.weight.DisplayPicsView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by n911305 on 2016/1/15.
 */
public abstract class ARecylclerReleaseListViewFragment extends BaseFragment implements AbsListView.RecyclerListener {

    public static final String TAG = ARecylclerReleaseListViewFragment.class.getSimpleName();

    @ViewInject(idStr = "listView")
    ListView recyclerView;
    List<ContentlistBean> datas;
    QuickAdapter quickAdapter;

    @Override
    protected int inflateContentView() {
        return R.layout.news_child_listview_fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void initData(List<ContentlistBean> datas){

        recyclerView.setAdapter(quickAdapter = new QuickAdapter<ContentlistBean>(getActivity(), R.layout.news_item_layout, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, ContentlistBean item) {

                helper.setText(R.id.title_textView, item.getTitle());
                helper.setText(R.id.content_textView, item.getDesc());
                DisplayPicsView displayPicsView = (DisplayPicsView) helper.getView().findViewById(R.id.display_view);
                if (!item.getImageurls().isEmpty()){
                    displayPicsView.setVisibility(View.VISIBLE);
                    ImageConfig imageConfig = new ImageConfig();
                    imageConfig.setLoadingRes(R.mipmap.ic_launcher);
                    displayPicsView.setPics(ARecylclerReleaseListViewFragment.this, item.getImageurls());
                }else{

                    displayPicsView.setVisibility(View.GONE);
                }
            }
        });
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
                    Log.d(TAG, "已经释放了，从Cache中移除");
                    viewCache.remove(view.toString());
                }
            }

            if (viewCache.size() > 0) {
                Set<String> keySet = viewCache.keySet();
                for (String key : keySet) {
                    View view = viewCache.get(key).get();
                    if (view != null) {
                        Log.d(TAG, "从Cache中释放一个View");

                        releaseImageView(view);
                    }
                }

//                viewCache.clear();
            }
        }
    }

    public void refreshUI() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
//                kRecyclerAdapter.notifyDataSetChanged();
            }

        }, 200);
    }

    protected boolean releaseImageView(View container) {

        DisplayPicsView displayPicsView = (DisplayPicsView) container.findViewById(R.id.display_view);
        if (displayPicsView != null){
            Log.d(TAG, "displayPicsView.release();");
            displayPicsView.release();
        }

        if (configCanReleaseIds() != null) {
            for (int imgId : configCanReleaseIds()) {
                ImageView imgView = (ImageView) container.findViewById(imgId);
                if (imgView != null) {
                    imgView.setImageDrawable(new ColorDrawable(Color.parseColor("#fff2f2f2")));
                    Log.d(ARecylclerReleaseListViewFragment.class.getSimpleName(), "释放ImageView");
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
    public void onMovedToScrapHeap(View view) {

        if (!viewCache.containsKey(view.toString())) {
            Log.d(TAG, view.toString() + "保存一个View到Cache");
            viewCache.put(view.toString(), new WeakReference<View>(view));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.postDelayed(releaseRunnable, 5 * 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.removeCallbacks(releaseRunnable);

        refreshUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory.......");
        if (BitmapLoader.getInstance() != null)
            BitmapLoader.getInstance().cancelPotentialTask(this);
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

    protected int[] configCanReleaseIds() {
        return null;
    }

    abstract ListView getReleaseView();

    Handler mHandler = new Handler() {

    };
}
