package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.bean.Categories;

public interface IHomeCallBack extends IBaseCallback {

    /**
     * 回调函数，UI通过它得到分类Categroies
     *
     * @param categories :传回来的分类
     */
    void onCategories(Categories categories);


}
