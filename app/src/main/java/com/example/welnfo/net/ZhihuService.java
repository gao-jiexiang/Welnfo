package com.example.welnfo.net;


import com.example.welnfo.bean.DailyNewsBean;
import com.example.welnfo.bean.NewsDetailBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZhihuService {
    String sBaseUrl = "http://news-at.zhihu.com/api/4/";

    //最新新闻
    @GET("news/latest")
    Flowable<DailyNewsBean> getLatest();

    //旧的新闻
    @GET("news/before/{date}")
    Flowable<DailyNewsBean> getOldNews(@Path("date") String date);

    @GET("news/{news_id}")
    Flowable<NewsDetailBean> getNewsDetail(@Path("news_id") int newsId);

}
