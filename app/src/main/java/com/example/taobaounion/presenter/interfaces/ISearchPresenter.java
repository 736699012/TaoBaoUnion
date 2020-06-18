package com.example.taobaounion.presenter.interfaces;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ISearchCallBcak;

public interface ISearchPresenter extends IBasePresenter<ISearchCallBcak> {

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 根据关键字进行搜索
     * @param keyword :
     */
    void doSearch(String keyword);

    /**
     * 搜索失败重新搜索
     */
    void research();

    /**
     * 获取更多搜索结果
     */
    void loadedMore();


    /**
     * 获取推荐词
     */
    void getRecommendWords();
}
