package com.example.welnfo.model;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.LoginBean;
import com.example.welnfo.bean.RegisterBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.util.ThreadManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class RegisterModel extends BaseModel {
    String successCode = BaseApp.getRes().getString(R.string.register_success);
    public void register(String name, String psd, final ResultCallBack<RegisterBean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                        .getLoginService()
                        .register(name, psd, "", "1")
                        .compose(RxUtils.<RegisterBean>rxSchedulerHelper())
                        .subscribeWith(new BaseSubscriber<RegisterBean>() {
                            @Override
                            public void onNext(RegisterBean registerBean) {
                                callBack.onSuccess(registerBean);
                            }
                        })
        );
    }

    public void registerAccess(final String uid, final String typeId, final String name, final String psd, final ResultCallBack<LoginBean> callBack) {
        addDisposable(
                HttpUtil.getInstance()
                        .getLoginService()
                        .register(name, psd, uid, typeId)
                        .compose(RxUtils.<RegisterBean>rxSchedulerHelper())
                        .subscribeWith(new BaseSubscriber<RegisterBean>() {
                            @Override
                            public void onNext(RegisterBean registerBean) {
                                //注册了一个账号
                                if (successCode.equals(registerBean.code)) {
                                    //注册成功,调用三方登录接口登录
                                    //loginAccess(uid,typeId,callBack);
                                    login(name,psd,callBack);
                                }else {
                                    //注册失败,一般来说是不会注册失败的,除非给的name和别人重了
                                    callBack.onFail(BaseApp.getRes().getString(R.string.name_repeat));
                                }
                            }
                        })
        );
    }

    //三方登录
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

    //账号密码的登录
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

    //环信
    public void registerEaseMob(final String name, final String psd, final ResultCallBack<String> callBack) {
        ThreadManager.getInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        //注册失败会抛出HyphenateException
                        try {
                            EMClient.getInstance().createAccount(name, psd);//同步方法
                            callBack.onSuccess("环信注册成功");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            LogUtil.print(e.toString());
                            callBack.onFail("环信注册失败");
                        }
                    }
                });
    }
}
