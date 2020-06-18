package com.example.taobaounion.presenter.interfaces;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.IOnSellCallBack;

public interface IOnSellPresenter extends IBasePresenter<IOnSellCallBack> {

    /**
     * 加载特惠内容
     */
    void getContent();

    /**
     * 加载更多内容
     */
    void getMoreContent();

    /**
     * 加载失败，重新加载
     */
    void reload();
}
