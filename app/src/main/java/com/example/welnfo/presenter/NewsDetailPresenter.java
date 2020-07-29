package com.example.welnfo.presenter;


import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.bean.NewsDetailBean;
import com.example.welnfo.model.NewsDetailModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.view.NewsDetailView;

public class NewsDetailPresenter extends BasePresenter<NewsDetailView> {

    private NewsDetailModel mNewsDetailModel;

    @Override
    protected void initModel() {
        mNewsDetailModel = new NewsDetailModel();
        addModel(mNewsDetailModel);
    }

    public void getNewsDetail(int newsId) {
        mNewsDetailModel.getNewsDetail(newsId, new ResultCallBack<NewsDetailBean>() {
            @Override
            public void onSuccess(NewsDetailBean newsDetailBean) {
                mView.setData(newsDetailBean);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
