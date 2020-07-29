package com.example.welnfo.model;


import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.NewsDetailBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;

public class NewsDetailModel extends BaseModel {
    public void getNewsDetail(int newsId, final ResultCallBack<NewsDetailBean> callBack) {

        addDisposable(
                HttpUtil.getInstance()
                .getZhihuService()
                .getNewsDetail(newsId)
                .compose(RxUtils.<NewsDetailBean>rxSchedulerHelper())
                .subscribeWith(new BaseSubscriber<NewsDetailBean>() {
                    @Override
                    public void onNext(NewsDetailBean newsDetailBean) {
                        callBack.onSuccess(newsDetailBean);
                    }
                })
        );
    }
}
