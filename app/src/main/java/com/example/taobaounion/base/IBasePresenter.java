package com.example.taobaounion.base;

public interface IBasePresenter<T> {

    /**
     * @param callback :被注册的回调方法
     */
    void registerViewCallBack(T callback);

    /**
     * @param callback 取消注册的回调方法
     */
    void unRegisterViewCallBack(T callback);
}
