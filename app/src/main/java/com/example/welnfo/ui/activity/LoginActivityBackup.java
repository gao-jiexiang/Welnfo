package com.example.welnfo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseActivity;
import com.example.welnfo.presenter.LoginPresenter;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.view.LoginView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;


import java.util.Map;

public class LoginActivityBackup extends BaseActivity<LoginPresenter> implements LoginView, View.OnClickListener {

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initData() {
        //String login = mPresenter.login(name,psd);
    }

    @Override
    protected void initView() {
        initPers();
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
    }

    private void initPers() {
        String[] pers = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this,pers,100);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login_backup;
    }


    //QQ与新浪
    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，
    // 需要在fragment依赖的Activity中实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                shareBoard();
                break;
            case R.id.btn2:
                share(SHARE_MEDIA.QQ);
                break;
            case R.id.btn3:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.btn4:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.btn5:
                login(SHARE_MEDIA.QQ);
                break;
            case R.id.btn6:
                login(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.btn7:
                login(SHARE_MEDIA.SINA);
                break;
        }
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
        for (Map.Entry<String,String> entry:data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            LogUtil.print(key+":"+value);
        }
    }

    private void share(SHARE_MEDIA type) {
        UMImage image = new UMImage(this, R.drawable.meizi2);//资源文件
        new ShareAction(this)
                .setPlatform(type)//传入平台
                .withText("hello")//分享内容
                .withMedia(image)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    //带面板的分享
    private void shareBoard() {
        UMImage image = new UMImage(this, R.drawable.meizi2);//资源文件
        new ShareAction(this)
                .withText("hello")//分享文本
                .withMedia(image)//分享媒体消息
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)//分享的平台
                .setCallback(shareListener)
                .open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToast("失败了");
            }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast("取消了");
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除授权,真实开发不要加这个,我们是为了每次都拉起授权界面
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, authListener);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void inputUserInfo(String uid, String typeId) {

    }


}
