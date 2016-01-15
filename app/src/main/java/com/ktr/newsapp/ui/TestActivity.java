package com.ktr.newsapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ktr.ktrsupportlibrary.bitmaploader.downloader.ImageDownloader;
import com.ktr.newsapp.R;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

//    01-15 10:11:39.804 6825-6825/com.ktr.newsapp I/imageUrl: http://upload.cankaoxiaoxi.com/2016/0115/1452819920183.jpg
//    01-15 10:11:39.838 6825-6825/com.ktr.newsapp I/imageUrl: http://upload.cankaoxiaoxi.com/2016/0113/1452650953299.jpg
//    01-15 10:11:39.990 6825-6825/com.ktr.newsapp I/imageUrl: http://imgedu.gmw.cn/attachement/jpg/site2/20160115/448a5bb24a341802887c60.jpg
//    01-15 10:11:40.040 6825-6825/com.ktr.newsapp I/imageUrl: http://imgworld.gmw.cn/attachement/png/site2/20160115/bc305bbef1b218027f250d.png
//    01-15 10:11:40.428 6825-6825/com.ktr.newsapp I/imageUrl: http://imgworld.gmw.cn/attachement/jpg/site2/20160115/bc305baf5b6618028c1614.jpg

    private static final String TAG = TestActivity.class.getSimpleName();

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        new ImageLoaderTask().execute();
    }


    class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {

                byte[] bitmapBytes = new ImageDownloader().downloadBitmap("http://upload.cankaoxiaoxi.com/2016/0115/1452819920183.jpg");

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

                Bitmap bitmap;
                try {

                    BitmapRegionDecoder bitmapDecoder = BitmapRegionDecoder.newInstance(bitmapBytes, 0, bitmapBytes.length, true);
                    Rect rect = new Rect(0, 0, options.outWidth, 180);
                    bitmap = bitmapDecoder.decodeRegion(rect, null).copy(Bitmap.Config.ARGB_8888, true);

                    Log.e(TAG, "size:" + getBitmapSize(bitmap));

                    return bitmap;

                } catch (IOException e) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        }
    }

    public int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
