package com.ktr.ktrsupportlibrary.fragmentmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Created by kisstherain on 2015/10/27.
 */
public abstract class FragmentCacheManager {

    FragmentActivity mActivity = null;

    SparseArray<Fragment> fragments = new SparseArray<Fragment>();

    FragmentTransaction fragmentTransaction = null;

    int mLayoutId;

    protected FragmentCacheManager(FragmentActivity fragmentActivity, int layoutId) {

        this.mActivity = fragmentActivity;

        this.mLayoutId = layoutId;

        initFragments(fragments);
    }

    /**
     * 切换Fragment
     * @param fragmentId 自定义id
     */
    public void changeFragmentByCache(int fragmentId) {

        for (int index = 0, size = fragments.size(); index < size; index ++){

            int key = fragments.keyAt(index);

            if (mActivity != null){

                fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
            }

            if (key == fragmentId) {

                Fragment fragment = fragments.get(fragmentId);

                if(!fragment.isAdded()){

                    FragmentTransaction ft_inner = null;

                    if (mActivity != null){

                        ft_inner = mActivity.getSupportFragmentManager().beginTransaction();
                    }

                    ft_inner.add(mLayoutId, fragment, fragment.getClass().getName());

                    ft_inner.commit();
                }

                fragmentTransaction.show(fragment);
            }else{

                fragmentTransaction.hide(fragments.get(key));
            }

            fragmentTransaction.commit();
        }
    }

    /**
     * 通过fragment自定义id查找
     * @param fragmentId
     * @return
     */
    public Fragment getFragmentById(int fragmentId){
        return fragments.get(fragmentId);
    }

    /**
     * 通过fragment Name 查找
     * @param fragmentName
     * @return
     */
    public Fragment getFragmentByName(String fragmentName){
        if (mActivity != null){
            return mActivity.getSupportFragmentManager().findFragmentByTag(fragmentName);
        }
        return null;
    }

    /**
     * 初始化Fragment list
     * @param fragments
     */
    public abstract void initFragments(SparseArray<Fragment> fragments);
}
