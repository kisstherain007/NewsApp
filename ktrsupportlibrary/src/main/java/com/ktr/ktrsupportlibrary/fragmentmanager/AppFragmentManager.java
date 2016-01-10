package com.ktr.ktrsupportlibrary.fragmentmanager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by zhoubo on 2015/1/10.
 *
 * 视图控制器Fragment管理类
 */
public class AppFragmentManager extends AppAbstractFragmentManager {

    FragmentActivity mActivity = null;

    public Fragment currentFragment = null;

    public AppFragmentManager(FragmentActivity activity){

        this.mActivity = activity;
    }

    /**
     * 切换Fragment并且去释放前一个碎片
     * @param fragment
     * @param layoutId
     */
    public void changeFragmentByRelease(Fragment fragment, int layoutId) {

        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();

        if (currentFragment != null && currentFragment == fragment) return;

        releaseFragment(currentFragment);

        currentFragment = fragment;

        fragmentTransaction.add(layoutId, fragment, fragment.getClass().getName());

        fragmentTransaction.commit();
    }

    public void releaseFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();

        if (fragment != null) {

            fragmentTransaction.remove(fragment);

            fragmentTransaction.commit();
        }
    }

}
