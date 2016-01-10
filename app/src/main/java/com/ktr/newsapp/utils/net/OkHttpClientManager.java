package com.ktr.newsapp.utils.net;

import android.util.Log;

import com.ktr.newsapp.api.ApiConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by kisstherain on 2016/1/9.
 */
public class OkHttpClientManager {

    private static final String TAG = "OkHttpClientManager";

    private static OkHttpClient sInstance;

    public static OkHttpClient getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (sInstance == null) {
//                    sInstance = new OkHttpClient();
                    //cookie enabled
//                    sInstance.newBuilder()..setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
////                    sInstance.setReadTimeout(15, TimeUnit.SECONDS);
////                    sInstance.setConnectTimeout(20, TimeUnit.SECONDS);
//                    sInstance.newBuilder().connectTimeout(20, TimeUnit.SECONDS);
//                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//                    sInstance.newBuilder().networkInterceptors().add(loggingInterceptor);
//                    sInstance.networkInterceptors().add(new HeaderInterceptor());
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    sInstance = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new LoggingInterceptor())
//                            .addNetworkInterceptor(loggingInterceptor)
                            .addNetworkInterceptor(new HeaderInterceptor())
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return sInstance;
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d(TAG, String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            Log.d(TAG, response.toString());

            return response;
        }
    }

    static class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("apikey", ApiConfig.API_STORE).build();
            return chain.proceed(request);
        }
    }
}
