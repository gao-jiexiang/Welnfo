package com.example.welnfo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.welnfo.R;
import com.example.welnfo.bean.ITbean;


import java.util.ArrayList;
import java.util.List;

public class ItAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<ITbean.ResultsBean> list;
    public ItAdapter(Context context, ArrayList<ITbean.ResultsBean> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.viewholder, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.tv.setText(list.get(position).getDesc());
        Glide.with(context).load(list.get(position).getUrl()).into(holder1.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<ITbean.ResultsBean> results) {
        list.addAll(results);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            tv=itemView.findViewById(R.id.tv);
        }
    }
}
