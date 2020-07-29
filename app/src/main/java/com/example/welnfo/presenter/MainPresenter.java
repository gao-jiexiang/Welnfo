package com.example.welnfo.presenter;


import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.model.MainModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.view.MainView;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.Map;

public class MainPresenter extends BasePresenter<MainView> {
    private MainModel mMainModel;


    @Override
    protected void initModel() {
        mMainModel = new MainModel();
        addModel(mMainModel);
    }


    public void getContactList() {
        mMainModel.getContactList(new ResultCallBack<Map<String, EaseUser>>() {
            @Override
            public void onSuccess(Map<String, EaseUser> contacts) {
                mView.setContactList(contacts);
            }

            @Override
            public void onFail(String msg) {
                mView.showToast(msg);
            }
        });
    }
}

