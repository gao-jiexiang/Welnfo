package com.example.welnfo.net;



import com.example.welnfo.bean.ItInfoArticle;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.bean.NaviBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WanService {
    String sUrl = "https://wanandroid.com/";

    @GET("wxarticle/chapters/json")
    Flowable<ItInfoTabBean> getTab();

    @GET("wxarticle/list/{id}/{page}/json")
    Flowable<ItInfoArticle> getWxArticles(@Path("id") int id, @Path("page") int page);

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Flowable<ItInfoArticle> query(@Field("k") String key, @Path("page") int page);

    @GET("navi/json")
    Flowable<NaviBean> getNaviData();
}
