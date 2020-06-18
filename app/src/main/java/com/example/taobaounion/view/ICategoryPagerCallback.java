package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.bean.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    /**
     * 加载成功得到数据
     *
     * @param contents
     */
    void onContentLoad(List<HomePagerContent.DataBean> contents);


    /**
     * @return :得到对应的ID
     */
    int getCategoryId();


    /**
     * 加载更多错误
     *
     * @param
     */
    void onLoaderMoreError();

    /**
     * 没有更多内容
     *
     * @param
     */
    void onLoaderMoreEmpty();


    /**
     * 加载到了更多内容
     *
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载到了
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);


}
