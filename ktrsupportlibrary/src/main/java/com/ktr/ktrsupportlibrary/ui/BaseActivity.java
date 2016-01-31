package com.ktr.ktrsupportlibrary.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ktr.ktrsupportlibrary.R;
import com.ktr.ktrsupportlibrary.common.setting.SettingUtility;
import com.ktr.ktrsupportlibrary.inject.InjectUtility;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kisstherain on 2016/1/9.
 */
public class BaseActivity extends AppCompatActivity {

    private BaseActivityHelper mHelper;

    private Toolbar mToolbar;

    // 当有Fragment Attach到这个Activity的时候，就会保存
    private Map<String, WeakReference<BaseFragment>> fragmentRefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (mHelper == null) {
            try {
                if (SettingUtility.getStringSetting("activity_helper") != null) {
                    mHelper = (BaseActivityHelper) Class.forName(SettingUtility.getStringSetting("activity_helper")).newInstance();
                    mHelper.bindActivity(this);
                }
            } catch (Exception e) {

            }
        }

        if (mHelper != null)
            mHelper.onCreate(savedInstanceState);

        fragmentRefs = new HashMap<String, WeakReference<BaseFragment>>();

        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        InjectUtility.initInjectedView(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mHelper != null) {
            boolean handle = mHelper.onKeyDown(keyCode, event);
            if (handle)
                return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (onBackClick())
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onBackClick() {
        if (mHelper != null) {
            boolean handle = mHelper.onBackClick();
            if (handle)
                return true;
        }

        Set<String> keys = fragmentRefs.keySet();
        for (String key : keys) {
            WeakReference<BaseFragment> fragmentRef = fragmentRefs.get(key);
            BaseFragment fragment = fragmentRef.get();
            if (fragment != null && fragment.onBackClick())
                return true;
        }

        finish();

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        RefWatcher refWatcher = NewsApp.getRefWatcher(this);
//        refWatcher.watch(this);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void addFragment(String tag, BaseFragment fragment) {
        fragmentRefs.put(tag, new WeakReference<BaseFragment>(fragment));
    }

    public void removeFragment(String tag) {
        fragmentRefs.remove(tag);
    }
}
