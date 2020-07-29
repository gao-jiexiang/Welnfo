package com.example.welnfo.net;

import android.database.Observable;


import com.example.welnfo.bean.Meizi;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    String sUrl = "https://gank.io/";

    @GET("api/data/%E7%A6%8F%E5%88%A9/10/{page}")
    Observable<Meizi> getMeizi(@Path("page") int page);

    //以后用这个
    //带背压策略的被观察者:当被观察者和观察者处理事务的速度不匹配时处理策略
    @GET("api/data/%E7%A6%8F%E5%88%A9/10/{page}")
    Flowable<Meizi> getMeizi2(@Path("page") int page);
}
