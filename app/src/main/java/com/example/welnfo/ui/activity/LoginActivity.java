package com.example.welnfo.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseActivity;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.FinishEvent;
import com.example.welnfo.presenter.LoginPresenter;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.util.SpUtil;
import com.example.welnfo.view.LoginView;
import com.hyphenate.chat.EMClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.iv_sina)
    ImageView ivSina;

    public static void starAct(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        /*String  token = (String) SpUtil.getParam(Constants.TOKEN, "");
        if (!TextUtils.isEmpty(token)){
            //登陆过
            go2MainActivity();
        }*/
        //之前是否登陆过
        if (EMClient.getInstance().isLoggedInBefore()){
            go2MainActivity();
        }
        initPre();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initPre() {
        String[] pers = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, pers, 100);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.btn_login, R.id.btn_register, R.id.iv_qq, R.id.iv_wechat, R.id.iv_sina})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.iv_qq:
                //login(SHARE_MEDIA.QQ);
                //
                String qqUnique = "qq3444497381";
                mPresenter.loginAccess(SHARE_MEDIA.QQ,qqUnique);
                break;
            case R.id.iv_wechat:
                login(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.iv_sina:
                login(SHARE_MEDIA.SINA);
                break;
        }
    }

    private void register() {
        RegisterActivity.startAct(this,RegisterActivity.TYPE_REGISTER);
    }

    private void login() {
        String name = etName.getText().toString().trim();
        String psd = etPsd.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd)){
            //只要有一个空(null,"")
            showToast(BaseApp.getRes().getString(R.string.name_or_psd_not_null));
            return;
        }

        mPresenter.login(name,psd);

    }

    //QQ与新浪
    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，
    // 需要在fragment依赖的Activity中实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void login(SHARE_MEDIA type) {
        UMShareAPI.get(this).getPlatformInfo(this, type, authListener);
    }

    //本质上三方登录最终都需要拉取三方平台的用户资料
    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            showToast("成功了");
            print(data);
            //data 里面有所有用户在三方平台的信息
            //判断拿到的唯一标识是否创建过用户 ??? 怎么判断
            //试着登录一下就可以判断出来
            mPresenter.loginAccess(platform,data.get("uid"));
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("失败");
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("取消");
        }
    };


    private void print(Map<String, String> data) {
        //map遍历 2种:
        //data.keySet(); 所有键的集合
        //data.entrySet(); 键值对的集合
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            LogUtil.print(key + ":" + value);
        }
    }


    @Override
    public void loginSuccess() {
        go2MainActivity();
    }

    //完善资料
    @Override
    public void inputUserInfo(String uid,String typeId) {
        RegisterActivity.startAct(this,RegisterActivity.TYPE_INPUT_INFO,uid,typeId);
    }

    private void go2MainActivity() {
        MainActivity.startAct(this);
        //关闭当前页面
        finish();
    }

    @Subscribe()
    public void onReceiveFinishEvent(FinishEvent event){
        finish();
    }
}
