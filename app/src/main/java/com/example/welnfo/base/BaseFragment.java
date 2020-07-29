package com.example.welnfo.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.welnfo.util.ToastUtil;
import com.example.welnfo.widget.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment<P extends BasePresenter>
        extends Fragment implements BaseView {

    private Unbinder mUnbinder;
    protected P mPresenter;
    private LoadingDialog mLoadingDialog;

    //由这个方法创建view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(getLayout(), null, false);
        mUnbinder = ButterKnife.bind(this, inflate);
        mPresenter = initPresenter();
        if (mPresenter != null){
            mPresenter.bindView(this);
        }
        //一般不会在这里写这两个方法
        /*initView(inflate);
        initData();*/
        return inflate;
    }


    //create:创建,
    //created:过去式,动作已经完成了
    //view 已经创建完成了,在onCreateView 之后调用的
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    protected abstract void initView(View inflate);

    protected abstract void initData();

    protected abstract int getLayout();

    protected abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        //界面销毁,取消网络请求
        //解除v层和P的关联
        mPresenter.destroy();
        mPresenter = null;
        hideLoading();
    }

    @Override
    public void showToast(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToastShort(msg);
            }
        });
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
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
