package com.example.taobaounion.presenter.interfaces;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 通过Id 去获取数据
     *
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    /**
     * 加载更多数据
     *
     * @param categoryId
     */
    void loaderMore(int categoryId);

    /**
     * 重新加载
     *
     * @param categoryId
     */
    void reload(int categoryId);

}
