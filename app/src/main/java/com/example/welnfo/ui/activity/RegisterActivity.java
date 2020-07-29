package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseActivity;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.FinishEvent;
import com.example.welnfo.presenter.RegisterPresenter;
import com.example.welnfo.view.RegisterView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.btn_register)
    Button btnRegister;

    //注册
    public static final int TYPE_REGISTER = 0;
    //完善资料
    public static final int TYPE_INPUT_INFO = 1;
    private int mType;
    private String mUid;
    private String mTypeId;

    public static void startAct(Context context,int type) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(Constants.TYPE,type);
        context.startActivity(intent);
    }

    public static void startAct(Context context, int type,String uid,String typeId) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.USERID,uid);
        intent.putExtra(Constants.TYPEID,typeId);
        context.startActivity(intent);
    }


    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra(Constants.TYPE, TYPE_REGISTER);
        mUid = getIntent().getStringExtra(Constants.USERID);
        mTypeId = getIntent().getStringExtra(Constants.TYPEID);
        if (mType==TYPE_REGISTER){
            toolbar.setTitle(R.string.register);
            btnRegister.setText(R.string.register);
        }else {
            toolbar.setTitle(R.string.input_info);
            btnRegister.setText(R.string.complete);
        }
        setSupportActionBar(toolbar);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked(View v) {
        switch (v.getId()){
            default:
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        String name = etName.getText().toString().trim();
        String psd = etPsd.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd)){
            showToast(BaseApp.getRes().getString(R.string.name_or_psd_not_null));
            return;
        }
        if (mType==TYPE_REGISTER){
            mPresenter.register(name,psd);
        }else {
            mPresenter.registerAccess(name,psd,mUid,mTypeId);
        }

    }

    @Override
    public void registerSuccess() {
        finish();
    }

    @Override
    public void loginSuccess() {
        showToast(BaseApp.getRes().getString(R.string.login_success));
        //完善资料之后登陆成功,结束当前页面,跳转到主页面
        go2MainActivity();
        finish();
        //应该把登录页面也finish掉,怎么通知 ??? Evenbus,广播,handler,
        EventBus.getDefault().post(new FinishEvent());
    }

    private void go2MainActivity() {
        MainActivity.startAct(this);
    }
}
