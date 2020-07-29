package com.example.welnfo.net;


import com.example.welnfo.bean.ITbean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface ItService {
    String url="https://gank.io/api/";
    @GET("data/福利/10/1")
    Flowable<ITbean> getItData();
}
