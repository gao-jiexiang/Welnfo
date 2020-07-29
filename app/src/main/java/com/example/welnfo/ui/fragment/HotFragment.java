package com.example.welnfo.ui.fragment;

import android.view.View;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.presenter.HotPresenter;
import com.example.welnfo.view.HotView;


public class HotFragment extends BaseFragment<HotPresenter> implements HotView {

    public static HotFragment getInstance(){
        HotFragment fragment = new HotFragment();
        return fragment;
    }
    @Override
    protected void initView(View inflate) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_hot;
    }

    @Override
    protected HotPresenter initPresenter() {
        return new HotPresenter();
    }
}
