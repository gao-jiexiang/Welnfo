package com.example.welnfo.net;

public interface ResultCallBack<T> {
    void onSuccess(T t);
    void onFail(String msg);
}

