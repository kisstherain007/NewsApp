package com.ktr.ktrsupportlibrary.bitmaploader.config;

import com.ktr.ktrsupportlibrary.bitmaploader.compress.IBitmapCompress;

/**
 * Created by n911305 on 2016/1/14.
 */
public class ImageConfig {

    private int maxWidth = 0;// 图片最大宽度

    private int maxHeight = 0;// 图片最大高度

    private Class<? extends IBitmapCompress> bitmapCompress;

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
