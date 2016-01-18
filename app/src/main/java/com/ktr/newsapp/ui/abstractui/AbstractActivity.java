package com.ktr.newsapp.ui.abstractui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.ktr.newsapp.NewsApp;
import com.ktr.newsapp.R;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by kisstherain on 2016/1/9.
 */
public class AbstractActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mToolbar = (Toolbar) findViewById(R.id.comm_toolbar);
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RefWatcher refWatcher = NewsApp.getRefWatcher(this);
        refWatcher.watch(this);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
