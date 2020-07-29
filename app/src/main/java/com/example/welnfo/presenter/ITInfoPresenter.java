package com.example.welnfo.presenter;


import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.model.ITInfoModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.view.ITInfoView;

public class ITInfoPresenter extends BasePresenter<ITInfoView> {

    private ITInfoModel mItInfoModel;

    @Override
    protected void initModel() {
        mItInfoModel = new ITInfoModel();
        addModel(mItInfoModel);
    }

    public void getTabData() {
        mItInfoModel.getTabData(new ResultCallBack<ItInfoTabBean>() {
            @Override
            public void onSuccess(ItInfoTabBean itInfoTabBean) {
                mView.setTab(itInfoTabBean);
            }

            @Override
            public void onFail(String msg) {

            }
        });

    }
}
