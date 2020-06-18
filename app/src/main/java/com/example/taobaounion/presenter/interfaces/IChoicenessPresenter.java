package com.example.taobaounion.presenter.interfaces;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.model.bean.ChoicenessCategories;
import com.example.taobaounion.view.IChoicenessCallBack;

public interface IChoicenessPresenter extends IBasePresenter<IChoicenessCallBack> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 通过分类获取内容
     * @param dataBean ：
     */
    void getContentByCategory(ChoicenessCategories.DataBean dataBean);

    /**
     * 重新加载内容
     */
    void reloadContent();

}
