package com.example.welnfo.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.welnfo.R;
import com.example.welnfo.bean.NaviBean;
import com.example.welnfo.ui.activity.NaviActivity;
import com.example.welnfo.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class RlvNaviAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<NaviBean.DataBean> list;
    private final LayoutInflater inflater;
    private int mRed = Color.parseColor("#ff0000");
    private int mGreen = Color.parseColor("#00ff00");
    private int mBlue = Color.parseColor("#EEC4F7");

    public RlvNaviAdapter(Context context, ArrayList<NaviBean.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navi, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        List<NaviBean.DataBean.ArticlesBean> articles = list.get(position).getArticles();
        for (int i = 0; i < articles.size(); i++) {
            TextView inflate = (TextView) inflater.inflate(R.layout.item_flowable, null,false);
            inflate.setText(articles.get(i).getTitle());
            if (i % 3==0){
                inflate.setTextColor(mRed);
            }else if (i %3 ==1){
                inflate.setTextColor(mGreen);
            }else {
                inflate.setTextColor(mBlue);
            }
            vh.fl.addView(inflate);
        }
    }

    public void setData(List<NaviBean.DataBean> data){
        list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder{

        private final FlowLayout fl;

        public VH(View itemView) {
            super(itemView);
            fl = itemView.findViewById(R.id.fl);
        }
    }
}
