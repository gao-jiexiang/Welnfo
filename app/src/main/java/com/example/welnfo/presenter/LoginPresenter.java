package com.example.welnfo.presenter;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.LoginBean;
import com.example.welnfo.model.LoginModel;
import com.example.welnfo.net.LoginService;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.util.SpUtil;
import com.example.welnfo.view.LoginView;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class LoginPresenter extends BasePresenter<LoginView> {

    private LoginModel loginModel;
    @Override
    protected void initModel() {
        loginModel=new LoginModel();
        addModel(loginModel);
    }

    public void login(String name, String psd) {
        /*loginModel.login(name,psd, new ResultCallBack<LoginBean>() {
            @Override
            public void onSuccess(LoginBean loginBean) {
                if (LoginService.LOGIN_SUCCESS_CODE.equals(loginBean.getCode())){
                    //成功
                    mView.loginSuccess();
                    //保存用户信息
                    saveUserData(loginBean);
                }else {
                    //失败
                    mView.showToast(BaseApp.getRes().getString(R.string.login_fail));
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });*/

        loginModel.loginEaseMod(name,psd, new ResultCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mView.showToast(s);
                mView.loginSuccess();
            }

            @Override
            public void onFail(String msg) {
                mView.showToast(msg);
            }
        });
    }

    //是否登录过的依据
    private void saveUserData(LoginBean loginBean) {
        LoginBean.UserBean user = loginBean.getUser();
        String token = loginBean.getToken();

        SpUtil.setParam(Constants.TOKEN,token);
        SpUtil.setParam(Constants.USERNAME,user.getName());
        SpUtil.setParam(Constants.USERID,user.getUserid());
        SpUtil.setParam(Constants.PHOTO,user.getHeaderpic());
    }

    public void loginAccess(SHARE_MEDIA platform, final String uid) {
        String typeId = LoginService.TYPE_QQ;
        if (platform ==SHARE_MEDIA.QQ){
            typeId=LoginService.TYPE_QQ;
        }else if (platform ==SHARE_MEDIA.WEIXIN){
            typeId=LoginService.TYPE_WECHAT;
        }else if (platform == SHARE_MEDIA.SINA){
            typeId = LoginService.TYPE_SINA;
        }

        final String finalTypeId = typeId;
        loginModel.loginAccess(uid,typeId, new ResultCallBack<LoginBean>() {
            @Override
            public void onSuccess(LoginBean loginBean) {
                if (LoginService.LOGIN_SUCCESS_CODE.equals(loginBean.getCode())){
                    //成功
                    mView.loginSuccess();
                    //保存用户信息
                    saveUserData(loginBean);
                }else {
                    mView.inputUserInfo(uid,finalTypeId);
                    mView.showToast(BaseApp.getRes().getString(R.string.input_info));
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
