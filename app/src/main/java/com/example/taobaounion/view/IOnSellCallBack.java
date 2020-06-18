package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.bean.OnSellContent;

public interface IOnSellCallBack extends IBaseCallback {

    /**
     * 加载特惠内容
     * @param content
     */
    void onContentLoaded(OnSellContent content);

    /**
     * 加载更多内容
     * @param moreContent
     */
    void onMoreLoaded(OnSellContent moreContent);

    /**
     * 加载更多内容失败
     */
    void onMoreLoadedError();

    /**
     * 加载更多内容为空
     */
    void onMoreLoadedEmpty();
}
