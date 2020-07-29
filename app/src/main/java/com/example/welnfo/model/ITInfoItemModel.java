package com.example.welnfo.model;


import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.ITbean;
import com.example.welnfo.bean.ItInfoArticle;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;

public class ITInfoItemModel extends BaseModel {
    public void getData(final ResultCallBack<ITbean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                        .getItService()
                        .getItData()
                        .compose(RxUtils.<ITbean>rxSchedulerHelper())
                        .subscribeWith(new BaseSubscriber<ITbean>() {
                            @Override
                            public void onNext(ITbean iTbean) {
                                callBack.onSuccess(iTbean);
                            }
                        })
        );
    }
}
