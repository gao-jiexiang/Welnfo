package com.example.welnfo.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.welnfo.R;
import com.example.welnfo.bean.ItInfoArticle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RlvSearchAdapter extends RecyclerView.Adapter {
    private ArrayList<ItInfoArticle.DataBean.DatasBean> mList;

    public RlvSearchAdapter(ArrayList<ItInfoArticle.DataBean.DatasBean> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        ItInfoArticle.DataBean.DatasBean bean = mList.get(position);
        vh.mTvName.setText(bean.getAuthor());
        vh.mTvTime.setText(bean.getNiceDate());
        vh.mTvTitle.setText(bean.getTitle());
        vh.mtvSrc.setText(bean.getSuperChapterName()+"."+bean.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addData(List<ItInfoArticle.DataBean.DatasBean> datas) {
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_src)
        TextView mtvSrc;
        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
