package com.example.welnfo.ui.fragment;

import android.view.View;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.presenter.SectionsPresenter;
import com.example.welnfo.view.SectionsView;


public class SectionsFragment extends BaseFragment<SectionsPresenter> implements SectionsView {

    public static SectionsFragment getInstance(){
        SectionsFragment fragment = new SectionsFragment();
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
        return R.layout.fragment_sections;
    }

    @Override
    protected SectionsPresenter initPresenter() {
        return new SectionsPresenter();
    }
}
