package com.example.welnfo.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.welnfo.R;
import com.example.welnfo.bean.DataBean;
import com.example.welnfo.bean.ItInfoTabBean;
import com.example.welnfo.widget.ItemCallback;

import java.util.ArrayList;
import java.util.Collections;

public class RlvInterestAdapter extends RecyclerView.Adapter implements ItemCallback {
    public ArrayList<DataBean> mList;

    public RlvInterestAdapter(ArrayList<DataBean> mList) {

        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interst, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        final DataBean dataBean = mList.get(position);
        vh.mTvTitle.setText(dataBean.getName());
        vh.mSc.setChecked(dataBean.getIsInterested());

        //swichcompat的状态切换监听
        vh.mSc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用户修改的状态需要保存下来
               if (buttonView.isPressed()){
                   //说明是用户行为，不是代码操作
                   dataBean.setIsInterested(isChecked);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }




    class VH extends RecyclerView.ViewHolder{

        private final TextView mTvTitle;
        private final SwitchCompat mSc;

        public VH(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mSc = itemView.findViewById(R.id.sc);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    //侧滑删除条目
    @Override
    public void onItemDelete(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }
}
