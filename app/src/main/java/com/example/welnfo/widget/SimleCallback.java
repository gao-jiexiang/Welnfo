package com.example.welnfo.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.welnfo.ui.adapter.RlvInterestAdapter;

public class SimleCallback extends ItemTouchHelper.Callback {
    private ItemCallback mCallBack;
    private boolean mDrag;
    private boolean mSwipe;

    public  SimleCallback(ItemCallback callback){

        this.mCallBack = callback;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int drag=ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipe =ItemTouchHelper.LEFT;
        return makeMovementFlags(drag,swipe);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mCallBack.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mCallBack.onItemDelete(viewHolder.getAdapterPosition());
    }

    //允许上下拖拽
    @Override
    public boolean isLongPressDragEnabled() {

        return mDrag;
    }

    public void setDragEnable(boolean dragEnable){
        this.mDrag =dragEnable;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mSwipe;
    }

    public void setSwipeEnable(boolean swipeEnable){
        this.mSwipe =swipeEnable;
    }
}
