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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Map<WeakReference<BitmapOwner>, List<WeakReference<ImageLoaderTask>>> ownerMap;

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

        ownerMap = new HashMap<WeakReference<BitmapOwner>, List<WeakReference<ImageLoaderTask>>>();
        taskCache = new HashMap<String, WeakReference<ImageLoaderTask>>();
        int memCacheSize = 1024 * 1024 * ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memCacheSize = memCacheSize / 3;

        Log.i(TAG, "memCacheSize = " + (memCacheSize / 1024 / 1024) + "MB");

        mImageCache = new BitmapCache(memCacheSize);
        bitmapProcess = new BitmapProcess();
    }

    private List<WeakReference<ImageLoaderTask>> getTaskCache(BitmapOwner owner) {
        List<WeakReference<ImageLoaderTask>> taskWorkInOwner = null;

        Set<WeakReference<BitmapOwner>> set = ownerMap.keySet();
        for (WeakReference<BitmapOwner> key : set)
            if (key != null && key.get() == owner)
                taskWorkInOwner = ownerMap.get(key);

        if (taskWorkInOwner == null) {
            taskWorkInOwner = new ArrayList<WeakReference<ImageLoaderTask>>();
            ownerMap.put(new WeakReference<BitmapOwner>(owner), taskWorkInOwner);
        }

        return taskWorkInOwner;
    }

    public void cancelPotentialTask(BitmapOwner owner) {
        if (owner == null)
            return;

        List<WeakReference<ImageLoaderTask>> taskWorkInFragment = getTaskCache(owner);
        if (taskWorkInFragment != null)
            for (WeakReference<ImageLoaderTask> taskRef : taskWorkInFragment) {
                ImageLoaderTask task = taskRef.get();
                if (task != null) {
                    task.cancel(true);
                    Log.d(TAG, String.format("fragemnt销毁，停止线程 url = %s", task.imageUrl));
                }
            }

        for (WeakReference<BitmapOwner> key : ownerMap.keySet())
            if (key != null && key.get() == owner) {
                ownerMap.remove(key);

                Log.w(TAG, "移除一个owner --->" + owner.toString());

                break;
            }

        Log.w(TAG, "owner%d个" + ownerMap.size());
    }

    public void display(BitmapOwner owner, ImageView imageView, String url, ImageConfig imageConfig){

        if (Utility.isEmpty(url)|| imageView == null) return;
        if (bitmapHasBeenSet(imageView, url)) return;

        CommonBitmap bitmap = mImageCache.getBitmapFromMemCache(url);

        if (bitmap != null){ // get from memory

            Log.i(TAG, "get from memory.....");

            imageView.setImageDrawable(new CommonDrawable(mContext.getResources(), bitmap, null));
        }else{

            if (!checkTaskExistAndRunning(url)) {

                ImageLoaderTask newTask = display(imageView, url, imageConfig);

                // 添加到fragment当中，当fragment在Destory的时候，清除task列表
                if (owner != null)
                    getTaskCache(owner).add(new WeakReference<ImageLoaderTask>(newTask));

                newTask = null; /***********有待验证作用.........***********/
            }
        }
    }

    public ImageLoaderTask display(ImageView imageView, String url, ImageConfig imageConfig){

//            Log.i(TAG, "get from net.....");
//
//            Log.i(TAG, url + "checkTaskExistAndRunning false.....");

            ImageLoaderTask imageLoaderTask = new ImageLoaderTask(imageView, url);
            WeakReference<ImageLoaderTask> taskReference = new WeakReference<ImageLoaderTask>(imageLoaderTask);
            taskCache.put(url, taskReference);

            setImageLoading(imageView, url, imageConfig);

            imageLoaderTask.execute();

            return imageLoaderTask;
    }

    public class ImageLoaderTask extends AsyncTask<Void, Void, CommonBitmap>{

        private List<WeakReference<ImageView>> imageViewsRef;
        String imageUrl;
        boolean isCompleted = false;

        ImageLoaderTask(ImageView imageView, String url){

            imageViewsRef = new ArrayList<WeakReference<ImageView>>();
            if (imageView != null)
                imageViewsRef.add(new WeakReference<ImageView>(imageView));
            this.imageUrl = url;
        }

        @Override
        protected CommonBitmap doInBackground(Void... params) {

            // 本地缓存读取

            // 网络下载
            try {

                byte[] bitmapBytes = new ImageDownloader().downloadBitmap(imageUrl);

                if (!isCancelled()){

                    CommonBitmap bitmap = bitmapProcess.compressBitmap(bitmapBytes, new ImageConfig());

                    if (bitmap != null){

                        mImageCache.addBitmapToMemCache(imageUrl, bitmap);

                        return bitmap;
                    }
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(CommonBitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                isCompleted = true;
                setImageBitmap(bitmap);
                taskCache.remove(imageUrl);
            }
        }

        void setImageBitmap(CommonBitmap bitmap){

            for (int i = 0; i < imageViewsRef.size(); i++){

                ImageView imageView = imageViewsRef.get(i).get();

                if (imageView != null){

                    Drawable drawable = imageView.getDrawable();

                    if (drawable != null && drawable instanceof CommonDrawable) {
                        CommonDrawable commonDrawable = (CommonDrawable) drawable;
//                        if (imageUrl.equals(aisenDrawable.getMyBitmap().getUrl())) {
//                            MyDrawable myDrawable = new MyDrawable(mContext.getResources(), bitmap, config, null);
//                            config.getDisplayer().loadCompletedisplay(imageView, myDrawable);
//                        }
                        imageView.setImageDrawable(new CommonDrawable(mContext.getResources(), bitmap, null));
                    }
                }
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

    private void setImageFaild(ImageView imageView, ImageConfig imageConfig) {
        if (imageView != null)
            imageView.setImageDrawable(
                    new CommonDrawable(mContext.getResources(), new CommonBitmap(imageConfig.getLoadfaildRes()), imageConfig));
    }

    private void setImageLoading(ImageView imageView, String url, ImageConfig imageConfig) {
        if (imageView != null)
            imageView.setImageDrawable(
                    new CommonDrawable(mContext.getResources(), new CommonBitmap(imageConfig.getLoadingRes(), url), imageConfig));
    }
}
