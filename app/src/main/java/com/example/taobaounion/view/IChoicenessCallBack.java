package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.bean.ChoicenessCategories;
import com.example.taobaounion.model.bean.ChoicenessContent;

public interface IChoicenessCallBack extends IBaseCallback {

    void onCategoriesLoad(ChoicenessCategories categories);

    void onContentLoad(ChoicenessContent content);
}
