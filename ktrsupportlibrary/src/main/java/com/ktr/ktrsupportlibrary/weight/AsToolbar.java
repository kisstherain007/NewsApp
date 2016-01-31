package com.ktr.ktrsupportlibrary.weight;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

/**
 * Created by kisstherain on 2015/8/30.
 */
public class AsToolbar extends Toolbar {

    static final String TAG = "AsToolbar";

    public AsToolbar(Context context) {
        super(context);
    }

    public AsToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(isInEditMode()) return;
    }

    private long lastClickTime = 0;
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        boolean handler = super.onTouchEvent(ev);
//
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            if (lastClickTime != 0) {
//                if (System.currentTimeMillis() - lastClickTime <= 500) {
//                    BaseActivity activity = BaseActivity.getRunningActivity();
//                    if (activity != null && activity instanceof OnToolbarDoubleClick)
//                        ((OnToolbarDoubleClick) activity).onToolbarDoubleClick();
//                }
//            }
//
//            lastClickTime = System.currentTimeMillis();
//        }
//
//        return handler;
//    }

    public interface OnToolbarDoubleClick {

        public boolean onToolbarDoubleClick();

    }
}
