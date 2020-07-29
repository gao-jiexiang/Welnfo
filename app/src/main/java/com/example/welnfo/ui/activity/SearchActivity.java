package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.welnfo.R;
import com.example.welnfo.bean.ItInfoArticle;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.RxUtils;
import com.example.welnfo.ui.adapter.RlvSearchAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Toolbar mTool;
    private MaterialSearchView mViewSearch;
    private RecyclerView mRlv;
    private RlvSearchAdapter mAdapter;
    private int mPage = 0;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mViewSearch.setMenuItem(item);

        return true;
    }

    private void initListener() {
        mViewSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交搜索,点击软键盘上的搜索按钮触发
                //ToastUtil.showToastShort("submit:"+query);
                submit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索文本内容发生改变的监听
                //ToastUtil.showToastShort("change:"+newText);
                return false;
            }
        });

        mViewSearch.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //搜索框显示的回调
                //ToastUtil.showToastShort("show");
            }

            @Override
            public void onSearchViewClosed() {
                //搜索框关闭的回调
                //ToastUtil.showToastShort("close");
            }
        });
    }

    private void submit(String query) {
        HttpUtil.getInstance()
                .getWanService()
                .query(query,mPage)
                .compose(RxUtils.<ItInfoArticle>rxSchedulerHelper())
                .subscribeWith(new BaseSubscriber<ItInfoArticle>() {
                    @Override
                    public void onNext(ItInfoArticle itInfoArticle) {
                        if (itInfoArticle != null && itInfoArticle.getData() != null
                                && itInfoArticle.getData().getDatas() != null &&
                                itInfoArticle.getData().getDatas().size()>0){
                            mAdapter.addData(itInfoArticle.getData().getDatas());
                        }

                    }
                });
    }

    private void initView() {
        mTool = (Toolbar) findViewById(R.id.tool);
        mViewSearch = (MaterialSearchView) findViewById(R.id.search_view);
        mRlv = (RecyclerView) findViewById(R.id.rlv);

        setSupportActionBar(mTool);

        mRlv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ItInfoArticle.DataBean.DatasBean> list = new ArrayList<>();
        mAdapter = new RlvSearchAdapter(list);
        mRlv.setAdapter(mAdapter);
    }


    //点击返回键的回调方法
    @Override
    public void onBackPressed() {
        if (mViewSearch.isSearchOpen()) {
            mViewSearch.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

}
