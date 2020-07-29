package com.example.welnfo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.welnfo.R;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.DataBean;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.ui.adapter.RlvInterestAdapter;
import com.example.welnfo.widget.SimleCallback;

import java.io.Serializable;
import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    private RecyclerView mRlv;
    private ArrayList<DataBean> mList;
    private RlvInterestAdapter adapter;

    public static void startAct(Activity context, ArrayList<DataBean> list){
        Intent intent = new Intent(context, InterestActivity.class);
        intent.putExtra(Constants.DATA,list);
        context.startActivityForResult(intent,100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        initView();
    }

    private void initView() {
        mList = (ArrayList<DataBean>)
                getIntent().getSerializableExtra(Constants.DATA);
        mRlv = (RecyclerView) findViewById(R.id.rlv);

        mRlv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RlvInterestAdapter(mList);
        mRlv.setAdapter(adapter);

        SimleCallback callback = new SimleCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        callback.setSwipeEnable(false);
        helper.attachToRecyclerView(mRlv);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA,adapter.mList);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}
