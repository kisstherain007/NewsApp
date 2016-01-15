package com.ktr.ktrsupportlibrary.bitmaploader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;
import com.ktr.ktrsupportlibrary.bitmaploader.downloader.BitmapCache;
import com.ktr.ktrsupportlibrary.bitmaploader.downloader.ImageDownloader;
import com.ktr.ktrsupportlibrary.utils.Utility;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kisstherain on 2016/1/13.
 *
 * 图片加载器
 */
public class BitmapLoader {

    public static String TAG = BitmapLoader.class.getSimpleName();

    private Context mContext;

    private static BitmapLoader ourInstance;

    private BitmapCache mImageCache;// 图片缓存

    private BitmapProcess bitmapProcess;

    private String imageCachePath;// 图片缓存路径

    private Map<String, WeakReference<ImageLoaderTask>> taskCache;

    private BitmapLoader(Context context) {
        mContext = context;
        init();
    }

    public static BitmapLoader getInstance() {
        return ourInstance;
    }

    public static BitmapLoader newInstance(Context context, String imageCachePath) {
        ourInstance = new BitmapLoader(context);
        ourInstance.imageCachePath = imageCachePath;
        return ourInstance;
    }

    private void init() {

        taskCache = new HashMap<String, WeakReference<ImageLoaderTask>>();
        int memCacheSize = 1024 * 1024 * ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memCacheSize = memCacheSize / 3;

        mImageCache = new BitmapCache(memCacheSize);
        bitmapProcess = new BitmapProcess();
    }

    public void display(ImageView imageView, String url){

        if (Utility.isEmpty(url)|| imageView == null) return;

        if (bitmapHasBeenSet(imageView, url)) return;

        Bitmap bitmap = mImageCache.getBitmapFromMemCache(url);

        if (bitmap != null){ // get from memory

            Log.i(TAG, "get from memory.....");

            imageView.setImageDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
        }else{

            if (!checkTaskExistAndRunning(url)) {

                Log.i(TAG, "get from net.....");

                Log.i(TAG, url + "checkTaskExistAndRunning false.....");

                ImageLoaderTask imageLoaderTask = new ImageLoaderTask(imageView, url);
                WeakReference<ImageLoaderTask> taskReference = new WeakReference<ImageLoaderTask>(imageLoaderTask);
                taskCache.put(url, taskReference);
                imageLoaderTask.execute();
            }else{

                Log.i(TAG, url + "checkTaskExistAndRunning true.....");
            }
        }
    }

    public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap>{

        ImageView imageView;
        String imageUrl;
        boolean isCompleted = false;

        ImageLoaderTask(ImageView imageView, String url){

            this.imageView = imageView;
            this.imageUrl = url;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            // 本地缓存读取

            // 网络下载
            try {

                byte[] bitmapBytes = new ImageDownloader().downloadBitmap(imageUrl);

                Bitmap bitmap = bitmapProcess.compressBitmap(bitmapBytes, new ImageConfig());

                if (bitmap != null){

                    mImageCache.addBitmapToMemCache(imageUrl, bitmap);

                    return bitmap;
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                isCompleted = true;
                imageView.setImageDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
                taskCache.remove(imageUrl);
            }
        }
    }

    public boolean checkTaskExistAndRunning(String url) {

        WeakReference<ImageLoaderTask> imageLoaderTask = taskCache.get(url);

        if (imageLoaderTask != null) {

            ImageLoaderTask oaderTask = imageLoaderTask.get();

            if (oaderTask != null) {

                if (!oaderTask.isCancelled() && !oaderTask.isCompleted && oaderTask.imageUrl.equals(url)) {

                    return true;
                }
            }
        } else {
            taskCache.remove(url);
        }

        return false;
    }

    public boolean bitmapHasBeenSet(ImageView imageView, String url) {

        if (imageView != null){

            Drawable drawable = imageView.getDrawable();

            if (drawable != null && !(drawable instanceof ColorDrawable)) {

                return true;
            } else {

                return false;
            }
        }

        return false;
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        new ClearImageCacheTask().execute();
    }

    class ClearImageCacheTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            if (mImageCache != null) {
                mImageCache.clearMemHalfCache();
            }
            return null;
        }
    }

}
