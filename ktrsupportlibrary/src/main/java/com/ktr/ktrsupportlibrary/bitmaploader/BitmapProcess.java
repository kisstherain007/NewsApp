package com.ktr.ktrsupportlibrary.bitmaploader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;
import com.ktr.ktrsupportlibrary.utils.ScreenUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.KeyGenerator;

/**
 * Created by n911305 on 2016/1/14.
 */
public class BitmapProcess {

    private FileDisk compFielDisk;// 保存压缩或者缩放后的图片

    private FileDisk origFileDisk;// 保存原始下载

    public BitmapProcess(String imageCachePath){

        compFielDisk = new FileDisk(imageCachePath + File.separator + "compression");
        origFileDisk = new FileDisk(imageCachePath + File.separator + "originate");
    }

    public CommonBitmap compressBitmap(byte[] bitmapBytes, String url, ImageConfig imageConfig){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        Bitmap bitmap = null;

        int maxWidth = imageConfig.getMaxWidth() == 0 ? ScreenUtil.getScreenWidth() / 6 : imageConfig.getMaxWidth();
        int maxHeight = imageConfig.getMaxHeight() == 0 ? ScreenUtil.getScreenHeight() / 10 : imageConfig.getMaxHeight();

            if (options.outWidth * 1.0f / options.outHeight > 2) {
                int reqHeight = maxHeight;

                // 截取局部图片
                BitmapRegionDecoder bitmapDecoder = null;
                try {

                    bitmapDecoder = BitmapRegionDecoder.newInstance(bitmapBytes, 0, bitmapBytes.length, true);
                    Rect rect = new Rect(0, 0, options.outWidth, reqHeight);
                    bitmap = bitmapDecoder.decodeRegion(rect, null).copy(Bitmap.Config.ARGB_8888, true);
                } catch (IOException e) {
                }
            } else {
//            bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes, maxWidth, maxHeight);

                imageConfig.setMaxWidth(maxWidth);
                imageConfig.setMaxHeight(maxHeight);

            bitmap = new TimelineThumbBitmapCompress().compress(bitmapBytes, imageConfig, options.outWidth, options.outHeight);
        }

        if (bitmap == null){

            bitmap = BitmapDecoder.decodeSampledBitmapFromByte(bitmapBytes);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//        byte[] bytes = out.toByteArray();

        return new CommonBitmap(bitmap, url);
    }

    /**
     * 从二级缓存获取位图数据
     *
     * @param url
     * @param config
     * @return
     * @throws Exception
     */
    public byte[] getBitmapFromCompDiskCache(String url, ImageConfig config) throws Exception {
        String key = url;

        return getBitmapFromDiskCache(url, key, compFielDisk, config);
    }

    private byte[] getBitmapFromDiskCache(String url, String key, FileDisk fileDisk, ImageConfig config) throws Exception {
        InputStream inputStream = fileDisk.getInputStream(url, key);

        if (inputStream == null)
            return null;

//        if (config.getProgress() != null)
//            config.getProgress().sendLength(inputStream.available());

        byte[] buffer = new byte[8 * 1024];
        int readLen = -1;
        int readBytes = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((readLen = inputStream.read(buffer)) != -1) {
            readBytes += readLen;
//            if (config.getProgress() != null)
//                config.getProgress().sendProgress(readBytes);
            outputStream.write(buffer, 0, readLen);
        }
        return outputStream.toByteArray();
    }

    public File getOirgFile(String url) {
        String key = url;

        return origFileDisk.getFile(url, key);
    }

    /**
     * 从原始缓存获取位图数据
     *
     * @param url
     * @param config
     * @return
     * @throws Exception
     */
    public byte[] getBitmapFromOrigDiskCache(String url, ImageConfig config) throws Exception {
        String key = url;

        return getBitmapFromDiskCache(url, key, origFileDisk, config);
    }

    /**
     * 将数据写入原始缓存
     *
     * @param bs
     */
    public void writeBytesToOrigDisk(byte[] bs, String url) throws Exception {
        String key = url;
        OutputStream out = origFileDisk.getOutputStream(url, key);

        ByteArrayInputStream in = new ByteArrayInputStream(bs);
        byte[] buffer = new byte[8 * 1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1)
            out.write(buffer, 0, len);

        out.flush();
        in.close();
        out.close();
        origFileDisk.renameFile(url, key);
    }
}
