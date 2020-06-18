package com.example.taobaounion.base;

public interface IBaseCallback {

    //    加载中
    void onLoading();

    //      加载错误
    void onError();

    //      加载内容为空
    void onEmpty();
}
