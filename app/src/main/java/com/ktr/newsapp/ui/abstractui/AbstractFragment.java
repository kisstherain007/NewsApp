package com.ktr.newsapp.ui.abstractui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktr.ktrsupportlibrary.inject.InjectUtility;
import com.ktr.newsapp.NewsApp;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by kisstherain on 2016/1/9.
 */
public abstract class AbstractFragment extends Fragment {

    private ViewGroup rootView;// 根视图

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(inflateContentView(), null);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        InjectUtility.initInjectedView(this, rootView);

        return rootView;
    }

    abstract protected int inflateContentView();

    @Override
    public void onDestroy() {
        super.onDestroy();

        RefWatcher refWatcher = NewsApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
