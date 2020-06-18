package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.bean.History;
import com.example.taobaounion.model.bean.SearchRecommend;
import com.example.taobaounion.model.bean.SearchResult;

import java.util.List;

public interface ISearchCallBcak extends IBaseCallback {

    /**
     * 搜索历史结果
     * @param histories
     */
    void onHistories(History histories);

    /**
     * 历史记录删除
     */
    void onHistoriesDeleted();

    /**
     * 搜索成功返回结果
     * @param result :
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载了更多内容
     * @param result :
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多内容时失败
     */
    void onMoreLoadedError();

    /**
     * 没有更多加载内容了
     */
    void onMoreLoadedEmpty();


    /**
     * 获取推荐关键字
     * @param recommendWords :
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
