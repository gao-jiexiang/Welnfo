package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.welnfo.R;
import com.example.welnfo.bean.NaviBean;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.RxUtils;
import com.example.welnfo.ui.adapter.RlvNaviAdapter;

import java.util.ArrayList;

import qdx.stickyheaderdecoration.NormalDecoration;

public class NaviActivity extends AppCompatActivity {

    private RecyclerView mRlv;
    private ArrayList<NaviBean.DataBean> list;
    private RlvNaviAdapter adapter;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, NaviActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        initView();
        initData();
    }

    private void initData() {
        HttpUtil.getInstance()
                .getWanService()
                .getNaviData()
                .compose(RxUtils.<NaviBean>rxSchedulerHelper())
                .subscribeWith(new BaseSubscriber<NaviBean>() {
                    @Override
                    public void onNext(NaviBean naviBean) {
                        if (naviBean.getData()!=null && naviBean.getData().size()>0){
                            adapter.setData(naviBean.getData());
                        }
                    }
                });

    }

    private void initView() {
        mRlv = (RecyclerView) findViewById(R.id.rlv);
        mRlv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new RlvNaviAdapter(this,list);
        mRlv.setAdapter(adapter);

        NormalDecoration decoration=new NormalDecoration() {
            @Override
            public String getHeaderName(int pos) {
                return list.get(pos).getName();
            }
        };
    }
}
