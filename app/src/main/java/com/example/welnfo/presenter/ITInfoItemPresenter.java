package com.example.welnfo.presenter;


import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.bean.ITbean;
import com.example.welnfo.bean.ItInfoArticle;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.model.DiscoveryModel;
import com.example.welnfo.model.ITInfoItemModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.view.ITInfoItemView;

public class ITInfoItemPresenter extends BasePresenter<ITInfoItemView> {
    private ITInfoItemModel itInfoItemModel;

    @Override
    protected void initModel() {
        itInfoItemModel = new ITInfoItemModel();
        addModel(itInfoItemModel);
    }

    public void getData() {
        itInfoItemModel.getData(new ResultCallBack<ITbean>() {
            @Override
            public void onSuccess(ITbean iTbean) {
                mView.setData(iTbean.getResults());
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
