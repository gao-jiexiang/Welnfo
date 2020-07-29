package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseActivity;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.DataBean;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.presenter.ITInfoPresenter;
import com.example.welnfo.ui.adapter.VpItInfoAdapter;
import com.example.welnfo.ui.fragment.ItInfoFragment;
import com.example.welnfo.view.ITInfoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItInfoActivity extends BaseActivity<ITInfoPresenter> implements ITInfoView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.pager)
    ViewPager mPager;
    private ArrayList<DataBean> mTabs;
    private ArrayList<BaseFragment> mFragments;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, ItInfoActivity.class));
    }

    @Override
    protected ITInfoPresenter initPresenter() {
        return new ITInfoPresenter();
    }

    @Override
    protected void initData() {
        mPresenter.getTabData();
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        mFragments = new ArrayList<>();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_it_info;
    }

    @Override
    public void setTab(ItInfoTabBean itInfoTabBean) {
        mTabs = itInfoTabBean.getData();
        initFragmens();
        initVp();
    }

    private void initFragmens() {
        mFragments.clear();
        for (int i = 0; i < mTabs.size(); i++) {
            DataBean dataBean = mTabs.get(i);
            if (dataBean.getIsInterested()){
                mFragments.add(ItInfoFragment.getInstance(mTabs.get(i).getId(),
                        mTabs.get(i).getName()));
            }
        }
    }

    @OnClick(R.id.iv)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv:
                InterestActivity.startAct(this,mTabs);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.it_info_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SearchActivity.startAct(this);
                break;
            case R.id.navi:
                //导航
                NaviActivity.startAct(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==100 &&  resultCode ==RESULT_OK  &&data!=null){
            mTabs = (ArrayList<DataBean>) data.getSerializableExtra(Constants.DATA);

            initFragmens();
            initVp();
        }
    }

    private void initVp() {
        //viewpager
        VpItInfoAdapter adapter = new VpItInfoAdapter(getSupportFragmentManager(),
                mTabs, mFragments);
        mPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mPager);
    }
}
