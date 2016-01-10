package com.ktr.newsapp.api;

import com.ktr.newsapp.bean.newsBean.NewsChannelBean;
import com.ktr.newsapp.bean.newsBean.NewsDetailBean;
import com.ktr.newsapp.utils.net.RetrofitManager;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kisstherain on 2016/1/9.
 */
public class ApiManager {

    public interface NewSApiService {

        @GET("channel_news")
        Call<NewsChannelBean> getChannel();

        @GET("search_news")
        Call<NewsDetailBean> getChannelBySearchChannelId(@Query("channelId") String channelId, @Query("page") int page);

        @GET("search_news")
        Call<NewsDetailBean> getChannelBySearch(@Query("channelId") String channelId, @Query("channelName") String channelName,
                                                @Query("title") String title, @Query("page") int page);

        @GET("channel_news")
        Observable<NewsChannelBean> getChannel2();
    }

    static NewSApiService newSApiService = RetrofitManager.getNewsRetrofit().create(NewSApiService.class);

    public static Call<NewsChannelBean> getChannelData() {
        return newSApiService.getChannel();
    }

    public static Call<NewsDetailBean> getDataByChannelId(String channelId, int page){
        return newSApiService.getChannelBySearchChannelId(channelId, page);
    }

    public static Observable<NewsChannelBean> getChannelData2() {
        return newSApiService.getChannel2();
    }
}
