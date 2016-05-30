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
import com.ktr.ktrsupportlibrary.utils.BitmapTask;
import com.ktr.ktrsupportlibrary.utils.Logger;
import com.ktr.ktrsupportlibrary.utils.Utility;

import java.io.File;
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
    }

    public static BitmapLoader getInstance() {
        return ourInstance;
    }

    static BitmapLoader newInstance(Context mContext) {
        ourInstance = new BitmapLoader(mContext);
        return ourInstance;
    }

    public static BitmapLoader newInstance(Context context, String imageCachePath) {
        BitmapLoader loader = newInstance(context);
        loader.imageCachePath = imageCachePath;
        loader.init();
        return loader;
    }

    private void init() {

        ownerMap = new HashMap<WeakReference<BitmapOwner>, List<WeakReference<ImageLoaderTask>>>();
        taskCache = new HashMap<String, WeakReference<ImageLoaderTask>>();
        int memCacheSize = 1024 * 1024 * ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        memCacheSize = memCacheSize / 3;

        Log.i(TAG, "memCacheSize = " + (memCacheSize / 1024 / 1024) + "MB");

        mImageCache = new BitmapCache(memCacheSize);
        bitmapProcess = new BitmapProcess(imageCachePath);
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

        Log.i(TAG, "taskCache size : " + taskCache.size());

        if (Utility.isEmpty(url)|| imageView == null) return;

        if (bitmapHasBeenSet(imageView, url)) return;

        CommonBitmap bitmap = mImageCache.getBitmapFromMemCache(url);

        if (bitmap != null){ // get from memory

            imageView.setImageDrawable(new CommonDrawable(mContext.getResources(), bitmap, null));
        }else{

            if (!checkTaskExistAndRunning(url, imageView)) {

                boolean canLoad = owner == null || owner.canDisplay() ? true : false;

                if (!canLoad) {
                    Logger.d(TAG, "视图在滚动，显示默认图片");

//                    setImageLoading(imageView, null, imageConfig);
                }else{

                    ImageLoaderTask newTask = display(imageView, url, imageConfig);

                    // 添加到fragment当中，当fragment在Destory的时候，清除task列表
                    if (owner != null) getTaskCache(owner).add(new WeakReference<ImageLoaderTask>(newTask));

                    newTask = null; /***********有待验证作用.........***********/
                }
            }
        }
    }

    public ImageLoaderTask display(ImageView imageView, String url, ImageConfig imageConfig){

//            Log.i(TAG, url + "checkTaskExistAndRunning false.....");

            ImageLoaderTask imageLoaderTask = new ImageLoaderTask(imageView, url, imageConfig);
            WeakReference<ImageLoaderTask> taskReference = new WeakReference<ImageLoaderTask>(imageLoaderTask);
            taskCache.put(url, taskReference);

            setImageLoading(imageView, url, imageConfig);

            imageLoaderTask.executrOnImageExecutor();

            return imageLoaderTask;
    }

    public class ImageLoaderTask extends BitmapTask<Void, Void, CommonBitmap> {

        private List<WeakReference<ImageView>> imageViewsRef;
        String imageUrl;
        boolean isCompleted = false;
        ImageConfig mImageConfig;

        ImageLoaderTask(ImageView imageView, String url, ImageConfig imageConfig){

            imageViewsRef = new ArrayList<WeakReference<ImageView>>();
            if (imageView != null)
                imageViewsRef.add(new WeakReference<ImageView>(imageView));
            this.imageUrl = url;
            this.mImageConfig = imageConfig;
        }

        @Override
        public CommonBitmap workInBackground(Void... params) throws Exception {

            // 本地缓存读取

            // 网络下载
            try {

                //
                byte[] bitmapBytes = doDownLoad(imageUrl, mImageConfig);

                if (!isCancelled() && checkImageBinding()){

                    CommonBitmap bitmap = bitmapProcess.compressBitmap(bitmapBytes, imageUrl, mImageConfig);

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
        protected void onTaskSuccess(CommonBitmap bitmap) {
            super.onTaskSuccess(bitmap);

            setImageBitmap(bitmap);
        }

        @Override
        protected void onTaskFailed(Exception exception) {
            super.onTaskFailed(exception);

//            if (config.getLoadfaildRes() > 0)
//                setImageBitmap(new MyBitmap(config.getLoadfaildRes()));
        }

        @Override
        protected void onTaskComplete() {
            super.onTaskComplete();

            isCompleted = true;

            taskCache.remove(imageUrl);
        }

        private boolean checkImageBinding() {
            for (int i = 0; i < imageViewsRef.size(); i++) {

                ImageView imageView = imageViewsRef.get(i).get();
                if (imageView != null) {

                    Drawable drawable = imageView.getDrawable();
                    if (drawable != null && drawable instanceof CommonDrawable) {

                        CommonDrawable aisenDrawable = (CommonDrawable) drawable;
                        if (imageUrl.equals(aisenDrawable.getMyBitmap().getUrl())) {
                            return true;
                        }
                    }
                }
            }
            return false;
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

    public boolean checkTaskExistAndRunning(String url, ImageView imageView) {

        WeakReference<ImageLoaderTask> imageLoaderTask = taskCache.get(url);

        if (imageLoaderTask != null) {

            ImageLoaderTask oaderTask = imageLoaderTask.get();

            if (oaderTask != null) {

                if (!oaderTask.isCancelled() && !oaderTask.isCompleted && oaderTask.imageUrl.equals(url)) {

                    oaderTask.imageViewsRef.add(new WeakReference<ImageView>(imageView));
                    Log.d(TAG, String.format("ImageView加载的图片已有线程在运行，url = %s", url));
                    return true;
                }
            }
        } else {
            taskCache.remove(url);
        }

        // 还没有线程，判断ImageView是否已经绑定了线程，如果绑定了，就将已存在的线程cancel(false)掉
        ImageLoaderTask task = getWorkingTask(imageView);
        if (task != null && !task.imageUrl.equals(url) && task.imageViewsRef.size() == 1) {
            Log.d(TAG, String.format("停止一个图片加载，如果还没有运行 url = %s", url));
            task.cancel(false);
        }

        return false;
    }

    private ImageLoaderTask getWorkingTask(ImageView imageView) {
        if (imageView == null)
            return null;

        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof CommonDrawable) {
            WeakReference<ImageLoaderTask> loader = ((CommonDrawable) drawable).getTask();
            if (loader != null && loader.get() != null)
                return loader.get();
        }
        return null;
    }

    public boolean bitmapHasBeenSet(ImageView imageView, String url) {

        if (imageView != null){

            Drawable drawable = imageView.getDrawable();

            if (drawable != null) {

                if (drawable instanceof CommonDrawable){
                    CommonDrawable commonDrawable = (CommonDrawable) drawable;
                    if (commonDrawable.getMyBitmap() != null && url.equals(commonDrawable.getMyBitmap().getUrl())){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public byte[] doDownLoad(String imageUrl, ImageConfig imageConfig) throws Exception {

        byte[] bitmapBytes = null;

        // 判断二级缓存数据
        bitmapBytes = bitmapProcess.getBitmapFromCompDiskCache(imageUrl, imageConfig);

        // 判断原始缓存数据
        if (bitmapBytes == null) {
            bitmapBytes = bitmapProcess.getBitmapFromOrigDiskCache(imageUrl, imageConfig);
            if (bitmapBytes != null) {
                Logger.v(TAG, "load the data through the original disk, url = " + imageUrl);
            }
        }

        // 网络加载
        if (bitmapBytes == null) {
            bitmapBytes = new ImageDownloader().downloadBitmap(imageUrl);
//            bitmapBytes = config.getDownloaderClass().newInstance().downloadBitmap(imageUrl, config);
            // 数据写入原始缓存
            if (bitmapBytes != null/* && config.isCacheEnable()*/)
                bitmapProcess.writeBytesToOrigDisk(bitmapBytes, imageUrl);
        }

        if (bitmapBytes != null){

            return bitmapBytes;
        }

        throw new Exception("download faild");
    }

    public File getCacheFile(String url) {
        return bitmapProcess.getOirgFile(url);
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
                mImageCache.clearMemCache();
            }

            Log.d(TAG, mImageCache.toString());

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
