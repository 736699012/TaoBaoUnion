package com.example.taobaounion.presenter.interfaces;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.IHomeCallBack;

/**
 * 首页的控制者
 */
public interface IHomePresenter extends IBasePresenter<IHomeCallBack> {

    /**
     * 获取分类
     */
    void getCategories();


}
