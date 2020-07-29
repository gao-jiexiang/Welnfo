package com.example.welnfo.base;

import java.util.ArrayList;

public abstract class BasePresenter<T extends BaseView> {
    public T mView;

    //存储子类的所有model
    private ArrayList<BaseModel> mModels;

    public BasePresenter(){
        initModel();
    }

    //子类复写,用来初始化M层对象的
    protected abstract void initModel();

    //v层和p绑定
    public void bindView(T view){
        mView = view;
    }

    //界面销毁的时候调用
    public void destroy() {
        //1.取消网络请求
        if (mModels != null && mModels.size()>0){
            for (int i = 0; i < mModels.size(); i++) {
                //通知M层界面销毁
                mModels.get(i).destroy();
            }
        }
        //2.解除v层和P的关联
        mView = null;
    }

    //将子类的m层对象保存起来
    public void addModel(BaseModel model){
        if (mModels == null){
            mModels = new ArrayList<>();
        }
        mModels.add(model);
    }

}

