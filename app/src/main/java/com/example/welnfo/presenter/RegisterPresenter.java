package com.example.welnfo.presenter;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.LoginBean;
import com.example.welnfo.bean.RegisterBean;
import com.example.welnfo.model.RegisterModel;
import com.example.welnfo.net.LoginService;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.util.SpUtil;
import com.example.welnfo.view.RegisterView;

public class RegisterPresenter extends BasePresenter<RegisterView> {

    private RegisterModel registerModel;
    private String registerSuccess = BaseApp.getRes().getString(R.string.register_success);
    @Override
    protected void initModel() {
        registerModel=new RegisterModel();
        addModel(registerModel);
    }

    public void register(String name, String psd) {
        /*registerModel.register(name,psd, new ResultCallBack<RegisterBean>() {
            @Override
            public void onSuccess(RegisterBean registerBean) {
                if (registerSuccess.equals(registerBean.code)) {
                    //注册成功
                    mView.registerSuccess();
                } else {
                    //注册失败
                }
                mView.showToast(registerBean.code);
            }

            @Override
            public void onFail(String msg) {

            }
        });*/

        //注册环信账号
        registerModel.registerEaseMob(name,psd, new ResultCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mView.showToast(s);
                mView.registerSuccess();
            }

            @Override
            public void onFail(String msg) {
                mView.showToast(msg);
            }
        });
    }

    //三方注册+三方登录
    public void registerAccess(String name, String psd, String uid, String typeId) {
        registerModel.registerAccess(uid,typeId,name, psd, new ResultCallBack<LoginBean>() {
            @Override
            public void onSuccess(LoginBean loginBean) {
                if (LoginService.LOGIN_SUCCESS_CODE.equals(loginBean.getCode())){
                    //成功
                    mView.loginSuccess();
                    //保存用户信息
                    saveUserData(loginBean);
                }else {
                    //一般是不会失败的
                    mView.showToast(BaseApp.getRes().getString(R.string.login_fail));
                }
            }

            @Override
            public void onFail(String msg) {
                mView.showToast(msg);
            }
        });
    }

    ////保存用户信息
    private void saveUserData(LoginBean loginBean) {
        LoginBean.UserBean user = loginBean.getUser();
        String token = loginBean.getToken();

        SpUtil.setParam(Constants.TOKEN,token);
        SpUtil.setParam(Constants.USERNAME,user.getName());
        SpUtil.setParam(Constants.USERID,user.getUserid());
        SpUtil.setParam(Constants.PHOTO,user.getHeaderpic());
    }
}
