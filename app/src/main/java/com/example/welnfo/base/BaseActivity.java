package com.example.welnfo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.welnfo.util.ToastUtil;
import com.example.welnfo.widget.LoadingDialog;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends BasePresenter>
        extends AppCompatActivity implements BaseView {

    //
    // protected BasePresenter mPresenter;
    protected T mPresenter;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //作为所有activity父类,布局不能写死了
        setContentView(getLayout());
        //避免进入页面EdiText自动弹出软键盘 且 避免底部控件被顶起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        mPresenter = initPresenter();
        //将通过构造传递的view层对象,使用方法传递了
        if (mPresenter != null){
            mPresenter.bindView(this);
        }
        initView();
        initData();
    }

    //子类复写,提供与自身匹配的p层对象
    protected abstract T initPresenter();

    //初始化数据
    protected abstract void initData();

    //找控件
    protected abstract void initView();

    //由子类复写,提供自己的布局
    protected abstract int getLayout();

    @Override
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToastShort(msg);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁,取消网络请求
        //解除v层和P的关联
        mPresenter.destroy();
        mPresenter = null;
        hideLoading();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()){
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null ){
            mLoadingDialog.dismiss();
        }
    }
}

