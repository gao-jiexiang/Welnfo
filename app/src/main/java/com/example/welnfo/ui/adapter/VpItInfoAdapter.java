package com.example.welnfo.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.bean.DataBean;
import com.example.welnfo.bean.ItInfoTabBean;

import java.util.ArrayList;
import java.util.List;

public class VpItInfoAdapter extends FragmentStatePagerAdapter {
    private final List<DataBean> mTabs;
    private final ArrayList<BaseFragment> mFragments;
    private final ArrayList<String> titles;

    public VpItInfoAdapter(FragmentManager fm, List<DataBean> tabs, ArrayList<BaseFragment> fragments) {
        super(fm);
        mTabs = tabs;
        mFragments = fragments;

        titles = new ArrayList<>();
        for (int i = 0; i < mTabs.size(); i++) {
            DataBean dataBean = mTabs.get(i);
            if (dataBean.getIsInterested()){
                titles.add(dataBean.getName());
            }

        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
