package com.example.welnfo.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseModel {
    //rxjava内置的容器,专门存放Disposable对象的
    CompositeDisposable mCompositeDisposable;

    public void destroy() {
        //销毁网络请求并且取消订阅关系
        if (mCompositeDisposable != null && mCompositeDisposable.size()>0){
            //1.调用容器中的所有的Disposable对象dispose();
            //2.会将容器清空
            mCompositeDisposable.clear();
        }
    }

    //将子类的Disposable 添加到容器中
    public void addDisposable(Disposable d){
        if (mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }

        mCompositeDisposable.add(d);
    }
}
