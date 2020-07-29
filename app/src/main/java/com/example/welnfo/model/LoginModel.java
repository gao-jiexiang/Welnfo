package com.example.welnfo.model;

import android.util.Log;

import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.LoginBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginModel extends BaseModel {
    public void login(String name, String psd, final ResultCallBack<LoginBean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                        .getLoginService()
                        .login(name, psd)
                        .compose(RxUtils.<LoginBean>rxSchedulerHelper())
                        .subscribeWith(new BaseSubscriber<LoginBean>() {
                            @Override
                            public void onNext(LoginBean loginBean) {
                                callBack.onSuccess(loginBean);
                            }
                        })
        );
    }

    public void loginAccess(String uid, String typeId, final ResultCallBack<LoginBean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                        .getLoginService()
                        .loginAccess(uid, typeId)
                        .compose(RxUtils.<LoginBean>rxSchedulerHelper())
                        .subscribeWith(new BaseSubscriber<LoginBean>() {
                            @Override
                            public void onNext(LoginBean loginBean) {
                                callBack.onSuccess(loginBean);
                            }
                        })
        );
    }

    public void loginEaseMod(String name, String psd, final ResultCallBack<String> callBack) {
        EMClient.getInstance().login(name,psd,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                callBack.onSuccess("环信登录成功");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
                callBack.onFail("登录失败"+message);
            }
        });
    }
}
