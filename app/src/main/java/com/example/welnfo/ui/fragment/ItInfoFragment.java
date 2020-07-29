package com.example.welnfo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.base.Constants;
import com.example.welnfo.bean.ITbean;
import com.example.welnfo.bean.ItInfoArticle;
import com.example.welnfo.presenter.ITInfoItemPresenter;
import com.example.welnfo.ui.adapter.ItAdapter;
import com.example.welnfo.ui.adapter.RlvSearchAdapter;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.view.ITInfoItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItInfoFragment extends BaseFragment<ITInfoItemPresenter> implements ITInfoItemView {
    /*@BindView(R.id.tv)
    TextView mTv;*/
    @BindView(R.id.recy)
    RecyclerView mRecy;
    private int mId;
    private int page=1;
    private ArrayList<ItInfoArticle.DataBean.DatasBean> mList;
    private ItAdapter adapter;

    public static ItInfoFragment getInstance(int id, String name) {
        ItInfoFragment fragment = new ItInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.DATA, id);
        bundle.putString(Constants.USERNAME, name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View inflate) {
        /*mId = getArguments().getInt(Constants.DATA);
        String name = getArguments().getString(Constants.USERNAME);
        mTv.setText(name);*/
        LogUtil.print("initView");

        mRecy.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<ITbean.ResultsBean> list = new ArrayList<>();
        adapter = new ItAdapter(getContext(),list);
        mRecy.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        mPresenter.getData();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_itinfo;
    }

    @Override
    protected ITInfoItemPresenter initPresenter() {
        return new ITInfoItemPresenter();
    }

    @Override
    public void setData(List<ITbean.ResultsBean> results) {
        adapter.addData(results);
    }
}
