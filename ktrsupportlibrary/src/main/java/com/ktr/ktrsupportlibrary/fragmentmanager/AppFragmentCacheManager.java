package com.ktr.ktrsupportlibrary.fragmentmanager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Created by zhoubo on 2015/1/10.
 *
 * 视图控制器Fragment管理类
 */
public abstract class AppFragmentCacheManager extends AppAbstractFragmentManager {

    FragmentActivity mActivity = null;

    Fragment mFragment = null;

    SparseArray<Fragment> fragments = new SparseArray<Fragment>();

	private int layoutRes;

	public AppFragmentCacheManager(FragmentActivity fragmentActivity, int layoutRes) {

        this.mActivity = fragmentActivity;
		this.layoutRes = layoutRes;
        initFragments(fragments, mActivity);
    }

	public AppFragmentCacheManager(Fragment fragment, int layoutRes) {

        this.mFragment = fragment;
		this.layoutRes = layoutRes;
        initFragments(fragments, mActivity);
    }

    /**
     * 切换Fragment
     * @param fragmentId
     */
    public void changeFragmentByCache(int fragmentId) {
    	
        if (fragments.size() == 0 ) return;
        
			for (int index = 0, size = fragments.size(); index < size; index ++){

				FragmentTransaction fragmentTransaction = null;
				
				if (mActivity != null){

			        fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
			    }else if (mFragment != null){

			        fragmentTransaction = mFragment.getChildFragmentManager().beginTransaction();
			    }
				
			    if (index == fragmentId) {

			        Fragment fragment = fragments.get(fragmentId);

			        if(!fragment.isAdded()){
			        	
			            FragmentTransaction ft_inner = null;

			            if (mActivity != null){

			                ft_inner = mActivity.getSupportFragmentManager().beginTransaction();
			            }else if (mFragment != null){

			                ft_inner = mFragment.getChildFragmentManager().beginTransaction();
			            }

			            ft_inner.add(layoutRes, fragment, fragment.getClass().getName());
			            
			            ft_inner.commit();
			            
			            if (mActivity != null){

			            	mActivity.getSupportFragmentManager().executePendingTransactions();
					    }else if (mFragment != null){

					        mFragment.getChildFragmentManager().executePendingTransactions();
					    }
			        }

			        fragmentTransaction.show(fragment);
			    }else{

			        fragmentTransaction.hide(fragments.get(index));
			    }
			    
			    fragmentTransaction.commit();
			    
			    if (mActivity != null){

	            	mActivity.getSupportFragmentManager().executePendingTransactions();
			    }else if (mFragment != null){

			        mFragment.getChildFragmentManager().executePendingTransactions();
			    }
			}
    }
    
    public Fragment getFragmentName(String name) {

    	if (mActivity != null){
    		
    		return mActivity.getSupportFragmentManager().findFragmentByTag(name);
    		
        }else if (mFragment != null){

        	return mFragment.getChildFragmentManager().findFragmentByTag(name);
        }
        
    	return null;
    }

    public abstract void initFragments(SparseArray<Fragment> fragments, FragmentActivity mActivity);
}
