package com.example.welnfo.presenter;


import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.bean.DailyNewsBean;
import com.example.welnfo.model.DailyNewsModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.view.DailyNewsView;

public class DailyNewsPresenter extends BasePresenter<DailyNewsView> {

    private DailyNewsModel mDailyNewsModel;

    @Override
    protected void initModel() {
        mDailyNewsModel = new DailyNewsModel();
        addModel(mDailyNewsModel);
    }

    public void getLatestNews() {
        mDailyNewsModel.getLatestNews(new ResultCallBack<DailyNewsBean>() {
            @Override
            public void onSuccess(DailyNewsBean dailyNewsBean) {
                mView.setData(dailyNewsBean);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
    public void getOldNews(String date) {
        mDailyNewsModel.getOldNews(date,new ResultCallBack<DailyNewsBean>() {
            @Override
            public void onSuccess(DailyNewsBean dailyNewsBean) {
                mView.setData(dailyNewsBean);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
