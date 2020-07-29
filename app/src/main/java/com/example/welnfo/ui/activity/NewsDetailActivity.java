package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.welnfo.R;
import com.example.welnfo.base.BaseActivity;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.DailyNewsBean;
import com.example.welnfo.bean.NewsDetailBean;
import com.example.welnfo.presenter.NewsDetailPresenter;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.view.NewsDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity<NewsDetailPresenter> implements NewsDetailView {

    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.ctl)
    CollapsingToolbarLayout mCtl;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    @BindView(R.id.wv)
    WebView mWv;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.cl)
    CoordinatorLayout mCl;
    private int mNewsId;

    public static void startAct(Context context, DailyNewsBean.StoriesBean bean) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(Constants.DATA, bean);
        context.startActivity(intent);
    }

    @Override
    protected NewsDetailPresenter initPresenter() {
        return new NewsDetailPresenter();
    }

    @Override
    protected void initData() {
        mPresenter.getNewsDetail(mNewsId);
    }

    @Override
    protected void initView() {
        DailyNewsBean.StoriesBean data = (DailyNewsBean.StoriesBean) getIntent().getSerializableExtra(Constants.DATA);
        mNewsId = data.getId();
        String title = data.getTitle();
        List<String> images = data.getImages();
        if (images != null && images.size()>0){
            Glide.with(this).load(images.get(0)).into(mIv);
        }

        //AppBarLayout 滑动偏移监听
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                /*verticalOffset:垂直方向的偏移量 0 到 -600 px*/
                LogUtil.print("verticalOffset:" + verticalOffset);
            }
        });

        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        mCtl.setTitle(title);
        //扩张时候的title颜色
        mCtl.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        //收缩后显示title的颜色
        mCtl.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void setData(NewsDetailBean newsDetailBean) {
        //html 片段
        String body = newsDetailBean.getBody();
        WebSettings settings = mWv.getSettings();
        //html 里面是有js代码的,让webview支持js
        settings.setJavaScriptEnabled(true);
        mWv.loadData(body,"text/html","utf-8");
        mWv.setWebViewClient(new WebViewClient());
        mWv.setWebChromeClient(new WebChromeClient());
        //扩大比例的缩放
        mWv.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWv.getSettings().setLoadWithOverviewMode(true);
    }
}
