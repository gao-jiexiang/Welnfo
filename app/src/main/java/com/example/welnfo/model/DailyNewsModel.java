package com.example.welnfo.model;


import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.DailyNewsBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;

public class DailyNewsModel extends BaseModel {
    public void getLatestNews(final ResultCallBack<DailyNewsBean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                .getZhihuService()
                .getLatest()
                .compose(RxUtils.<DailyNewsBean>rxSchedulerHelper())
                .subscribeWith(new BaseSubscriber<DailyNewsBean>() {
                    @Override
                    public void onNext(DailyNewsBean dailyNewsBean) {
                        callBack.onSuccess(dailyNewsBean);
                    }
                })
        );
    }

    public void getOldNews(String date,final ResultCallBack<DailyNewsBean> callBack){
        addDisposable(
                HttpUtil.getInstance()
                        .getZhihuService()
                        .getOldNews(date)
                        .compose(RxUtils.<DailyNewsBean>rxSchedulerHelper())
                        .subscribeWith(new BaseSubscriber<DailyNewsBean>() {
                            @Override
                            public void onNext(DailyNewsBean dailyNewsBean) {
                                callBack.onSuccess(dailyNewsBean);
                            }
                        })
        );
    }
}
