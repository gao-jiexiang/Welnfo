package com.example.welnfo.model;


import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;

public class ITInfoModel extends BaseModel {
    public void getTabData(final ResultCallBack<ItInfoTabBean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                .getWanService()
                .getTab()
                .compose(RxUtils.<ItInfoTabBean>rxSchedulerHelper())
                .subscribeWith(new BaseSubscriber<ItInfoTabBean>() {
                    @Override
                    public void onNext(ItInfoTabBean itInfoTabBean) {
                        callBack.onSuccess(itInfoTabBean);
                    }
                })
        );
    }
}
