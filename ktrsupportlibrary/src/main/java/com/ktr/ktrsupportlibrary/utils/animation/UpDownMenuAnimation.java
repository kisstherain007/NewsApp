package com.ktr.ktrsupportlibrary.utils.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


public class UpDownMenuAnimation {

	private boolean menuShowed;
	
	private static UpDownMenuAnimation instance = null;
	
	private Animation showAction, hideAction;
	
	public static UpDownMenuAnimation getInstance(){
		
		if(instance == null){
			
			instance = new UpDownMenuAnimation();
		}
		
		return instance;
	}
	
	private UpDownMenuAnimation(){
		
		init();
	}

	private void init() {
		
		// 这里是TranslateAnimation动画
        showAction = new TranslateAnimation
        		( 	Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 1.0f, 
					Animation.RELATIVE_TO_SELF, 0.0f);
        // 这里是ScaleAnimation动画
      //showAction = new ScaleAnimation( 
        // 1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
        // Animation.RELATIVE_TO_SELF, 0.0f); 
        showAction.setDuration(200);
        
     // 这里是TranslateAnimation动画 
        hideAction = new TranslateAnimation
    		( 		Animation.RELATIVE_TO_SELF, 0.0f, 
    				Animation.RELATIVE_TO_SELF, 0.0f, 
    				Animation.RELATIVE_TO_SELF, 0.0f, 
    				Animation.RELATIVE_TO_SELF, 1.0f);
   
     // 这里是ScaleAnimation动画
  /*      hideAction = new ScaleAnimation( 
        			1.0f, 1.0f, 
        			1.0f, 0.0f, 
        			Animation.RELATIVE_TO_SELF, 0.0f, 
        			Animation.RELATIVE_TO_SELF, 0.0f);*/
        
        
        hideAction.setDuration(200);
        menuShowed = false; 
    }
	
	public void show(View view){
		
//		if(view.getVisibility() == View.VISIBLE) return;
		
		menuShowed = !menuShowed; 
		view.startAnimation(showAction);
		view.setVisibility(View.VISIBLE);
	}
	
	public void hide(View view){
		
//		if(view.getVisibility() == View.GONE) return;
		
		menuShowed = !menuShowed;
		view.startAnimation(hideAction); 
		view.setVisibility(View.GONE); 
	}
}
