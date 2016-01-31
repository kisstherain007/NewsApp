package com.ktr.newsapp.weight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ktr.ktrsupportlibrary.R;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;
import com.ktr.ktrsupportlibrary.ui.BaseFragment;
import com.ktr.ktrsupportlibrary.utils.Logger;
import com.ktr.ktrsupportlibrary.utils.ScreenUtil;
import com.ktr.ktrsupportlibrary.utils.animation.AnimatorUtils;
import com.ktr.newsapp.bean.newsBean.Imageurls;

import java.io.File;
import java.util.List;


/**
 * Created by kisstherain on 2015/10/31.
 */
public class DisplayPicsView extends ViewGroup {

    public static final String TAG = DisplayPicsView.class.getSimpleName();

    private int gap;

    private Rect[] picRects;

    private int screenWidth;

    private int screenHeight;

    private List<Imageurls> mUrlList;
    private BaseFragment mUownerFragment;

    public DisplayPicsView(Context context) {
        super(context);

    }

    public DisplayPicsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DisplayPicsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        screenWidth = ScreenUtil.getScreenWidth();
        screenHeight = ScreenUtil.getScreenHeight();
        gap = getResources().getDimensionPixelSize(R.dimen.gap_pics);
    }

    int mWidth;

    public void setPics(BaseFragment ownerFragment, List<Imageurls> urlList) {

        this.mUownerFragment = ownerFragment;
        this.mUrlList = urlList.size() <= 9 ? urlList : urlList.subList(0, 9);

//        int mWidth =  Math.round(screenWidth * 1.0f * 1 / 2); // small
//        int mWidth = ScreenUtil.getScreenWidth() - 2 * getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin); // large

        // View width,height
        int maxWidth = ScreenUtil.getScreenWidth() - ScreenUtil.dip2px(18 * 2);
        mWidth = Math.round(maxWidth * 1.0f * 4 / 5);
        int mHeight = mWidth;

        // child image width,height
        int imgW = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
        int imgH = imgW;

        LinearLayout.LayoutParams layoutParams = null;
        Rect rect;
        Rect[] tempRects = new Rect[mUrlList.size()];
        layoutParams = (LinearLayout.LayoutParams) getLayoutParams();

        if(layoutParams == null){

            layoutParams = new LinearLayout.LayoutParams(mWidth, mHeight);
        }else{
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
        }

        switch (mUrlList.size()){
            case 1:
                imgW = mWidth;
                imgH = Math.round(imgW * 4.0f / 3.0f);

                String url = mUrlList.get(0).getUrl();
                File file = BitmapLoader.getInstance().getCacheFile(url);
                if (file != null && file.exists()) {
                    long time = System.currentTimeMillis();
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                    Logger.w(String.format("耗时%s毫秒", String.valueOf(System.currentTimeMillis() - time)));
                    imgH = Math.round(imgW * opts.outHeight * 1.0f / opts.outWidth);
                    if (imgH > ScreenUtil.getScreenHeight())
                        imgH = imgW;
                }

                rect  = new Rect(0, 0,imgW, imgH);
                tempRects[0] = rect;

                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
                break;
            case 2:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                break;
            case 3:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;
                break;
            case 4:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 2 + gap);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;

                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[3] = rect;
                break;
            case 5:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 2 + gap);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;

                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[3] = rect;
                rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
                tempRects[4] = rect;
                break;
            case 6:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 2 + gap);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;

                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[3] = rect;
                rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
                tempRects[4] = rect;
                rect = new Rect((imgW + gap) * 2, imgH + gap, imgW * 3 + gap * 2, imgH * 2 + gap);
                tempRects[5] = rect;
                break;
            case 7:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 3 + gap);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;

                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[3] = rect;
                rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
                tempRects[4] = rect;
                rect = new Rect((imgW + gap) * 2, imgH + gap, imgW * 3 + gap * 2, imgH * 2 + gap);
                tempRects[5] = rect;

                rect = new Rect(0, (imgH + gap) * 2, imgW, imgH * 3 + gap);
                tempRects[6] = rect;
                break;
            case 8:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 3 + gap);
                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;

                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[3] = rect;
                rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
                tempRects[4] = rect;
                rect = new Rect((imgW + gap) * 2, imgH + gap, imgW * 3 + gap * 2, imgH * 2 + gap);
                tempRects[5] = rect;

                rect = new Rect(0, (imgH + gap) * 2, imgW, imgH * 3 + gap);
                tempRects[6] = rect;
                rect = new Rect(imgW + gap, (imgH + gap) * 2, imgW * 2 + gap, imgH * 3 + gap);
                tempRects[7] = rect;
                break;
            case 9:
                layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 3 + gap);

                rect = new Rect(0, 0, imgW, imgH);
                tempRects[0] = rect;
                rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
                tempRects[1] = rect;
                rect = new Rect((imgW + gap) * 2, 0, imgW * 3 + gap * 2, imgH);
                tempRects[2] = rect;

                rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
                tempRects[3] = rect;
                rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
                tempRects[4] = rect;
                rect = new Rect((imgW + gap) * 2, imgH + gap, imgW * 3 + gap * 2, imgH * 2 + gap);
                tempRects[5] = rect;

                rect = new Rect(0, (imgH + gap) * 2, imgW, imgH * 3 + gap);
                tempRects[6] = rect;
                rect = new Rect(imgW + gap, (imgH + gap) * 2, imgW * 2 + gap, imgH * 3 + gap);
                tempRects[7] = rect;
                rect = new Rect((imgW + gap) * 2, (imgH + gap) * 2, imgW * 3 + gap * 2, imgH * 3 + gap);
                tempRects[8] = rect;
                break;
        }

        setLayoutParams(layoutParams);

        picRects = tempRects;

        displayPics();

        requestLayout();
    }

    private void displayPics() {

        if (picRects == null || mUrlList == null) return;

        for (int i = 0; i < getChildCount(); i++){

            ImageView imgView = (ImageView) getChildAt(i);

            if(i >= picRects.length){

                getChildAt(i).setVisibility(View.GONE);
            }else{
                imgView.setVisibility(View.VISIBLE);
                ImageConfig imageConfig = new ImageConfig();
                imageConfig.setLoadingRes(com.ktr.newsapp.R.mipmap.ic_launcher);
                BitmapLoader.getInstance().display(mUownerFragment, imgView, mUrlList.get(i).getUrl(), imageConfig);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (picRects == null || mUrlList == null) return;

        int count = getChildCount();

        for (int i = 0; i < count; i ++){

            if(i < picRects.length){
                final Rect imgRect = picRects[i];
                final ImageView childView = (ImageView) getChildAt(i);
                childView.layout(imgRect.left, imgRect.top, imgRect.right, imgRect.bottom);
            }else{
                break;
            }
        }
    }

    public void release() {
        Logger.v(TAG, "释放资源");

        for (int i = 0; i < getChildCount(); i++) {
            ImageView imgView = (ImageView) getChildAt(i);
            imgView.setImageDrawable(new ColorDrawable(Color.parseColor("#fff2f2f2")));
        }
    }
}
