package com.example.welnfo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.bean.DailyNewsBean;
import com.example.welnfo.presenter.DailyNewsPresenter;
import com.example.welnfo.ui.activity.CalendarActivity;
import com.example.welnfo.ui.activity.NewsDetailActivity;
import com.example.welnfo.ui.adapter.RlvDailyNewsAdapter;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.util.TimeUtil;
import com.example.welnfo.view.DailyNewsView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DailyNewsFragment extends BaseFragment<DailyNewsPresenter> implements DailyNewsView {

    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private RlvDailyNewsAdapter mAdapter;

    public static DailyNewsFragment getInstance() {
        DailyNewsFragment fragment = new DailyNewsFragment();
        return fragment;
    }

    @Override
    protected void initView(View inflate) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        rlv.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<DailyNewsBean.TopStoriesBean> bannerList = new ArrayList<>();
        ArrayList<DailyNewsBean.StoriesBean> newsList = new ArrayList<>();
        mAdapter = new RlvDailyNewsAdapter(getContext(), bannerList, newsList);
        rlv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RlvDailyNewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                NewsDetailActivity.startAct(getContext(),mAdapter.mNewsList.get(position));
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getLatestNews();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_daily_news;
    }

    @Override
    protected DailyNewsPresenter initPresenter() {
        return new DailyNewsPresenter();
    }

    @OnClick(R.id.fab)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.fab:
                startActivity(new Intent(getContext(), CalendarActivity.class));
                break;
        }
    }

    @Override
    public void setData(DailyNewsBean dailyNewsBean) {
        mAdapter.setData(dailyNewsBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onReceiveDate(CalendarDay day){
        LogUtil.print("收到日期:"+day.toString());
        //收到日期,根据日期请求对应日期的新闻
        /*if (收到的日期==今天){
            mPresenter.getLatestNews();
        }else {
            mPresenter.getOldNews(date);
        }*/
        //这个日历指向的日期是我们选中的日期
        Calendar calendar = day.getCalendar();
        //选中的日期
        String selectTime = TimeUtil.parseTime(calendar);
        if (TimeUtil.checkTimeIsTotay(selectTime)) {
            mPresenter.getLatestNews();
        }else {
            //给的新闻是给定日期前一天的新闻,比如给了20200601,给的新闻是0531
            //传给后端的日期需要在原来基础上+1天
            calendar.add(Calendar.DAY_OF_MONTH,1);
            selectTime= TimeUtil.parseTime(calendar);

            mPresenter.getOldNews(selectTime);
        }
    }
}
