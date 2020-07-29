package com.example.welnfo.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.welnfo.base.BaseFragment;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;

public class VpZhihuAdpater extends FragmentPagerAdapter {
    private final ArrayList<BaseFragment> mFragments;
    private final ArrayList<String> mTitles;

    public VpZhihuAdpater(FragmentManager fm, ArrayList<BaseFragment> mFragments, ArrayList<String> mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    //TabLayout和viewpager配合使用的时候tab是由适配器帮忙创建的
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
