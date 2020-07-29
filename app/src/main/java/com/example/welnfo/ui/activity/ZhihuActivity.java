package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.ui.adapter.VpZhihuAdpater;
import com.example.welnfo.ui.fragment.DailyNewsFragment;
import com.example.welnfo.ui.fragment.HotFragment;
import com.example.welnfo.ui.fragment.SectionsFragment;

import java.util.ArrayList;

public class ZhihuActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mVp;
    private ArrayList<String> mTitles;
    private ArrayList<BaseFragment> mFragments;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, ZhihuActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu);
        initView();
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mVp = (ViewPager) findViewById(R.id.vp);
        initTitle();
        initFragments();

        VpZhihuAdpater adapter = new VpZhihuAdpater(getSupportFragmentManager(), mFragments, mTitles);
        mVp.setAdapter(adapter);

        //关联TabLayout和viewpager
        mTabLayout.setupWithViewPager(mVp);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();

        mFragments.add(DailyNewsFragment.getInstance());
        mFragments.add(SectionsFragment.getInstance());
        mFragments.add(HotFragment.getInstance());
    }

    private void initTitle() {
        mTitles = new ArrayList<>();
        mTitles.add(BaseApp.getRes().getString(R.string.daily_news));
        mTitles.add(BaseApp.getRes().getString(R.string.sections));
        mTitles.add(BaseApp.getRes().getString(R.string.hot));
    }
}
