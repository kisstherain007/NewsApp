package com.ktr.ktrsupportlibrary.bitmaploader.compress;

import android.graphics.Bitmap;

import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;

public interface IBitmapCompress {

	public Bitmap compress(byte[] bitmapBytes, ImageConfig config, int origW, int origH) throws Exception;
}
