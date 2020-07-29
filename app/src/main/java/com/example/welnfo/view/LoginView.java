package com.example.welnfo.view;

import com.example.welnfo.base.BaseView;

public interface LoginView extends BaseView {
    void loginSuccess();

    //完善资料
    void inputUserInfo(String uid, String typeId);
}
