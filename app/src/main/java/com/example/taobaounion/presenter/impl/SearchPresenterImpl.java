package com.example.taobaounion.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.bean.History;
import com.example.taobaounion.model.bean.SearchRecommend;
import com.example.taobaounion.model.bean.SearchResult;
import com.example.taobaounion.presenter.interfaces.ISearchPresenter;
import com.example.taobaounion.utils.JsonCacheUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManger;
import com.example.taobaounion.view.ISearchCallBcak;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {

    private Api mApi;
    private ISearchCallBcak mCallback;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private String mKeyword;
    private final JsonCacheUtil mJsonCacheUtil;


    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManger.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }


    @Override
    public void getHistories() {
        History mHistories = mJsonCacheUtil.getValue(KEY_HISTORIES, History.class);
        if (mCallback != null ) {
            mCallback.onHistories(mHistories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
        if (mCallback != null) {
            History history = mJsonCacheUtil.getValue(KEY_HISTORIES, History.class);
            mCallback.onHistories(history);
        }
    }

    private static final String KEY_HISTORIES = "key_histories";
    private static final int DEFAULT_SIZE = 10;
    private int mCurrentSize = DEFAULT_SIZE;

    /**
     * 添加历史记录
     *
     * @param history :
     */
    private void saveHistories(String history) {

        History histories = mJsonCacheUtil.getValue(KEY_HISTORIES, History.class);
//        如果已经存在了，先删除，在加入
        List<String> historyList = null;
        if (histories != null && histories.getHistoryList() != null) {
            historyList = histories.getHistoryList();
            if (historyList.contains(history)) {
                historyList.remove(history);
            }
        }
//        已经去重了
//        该添加了
        if (historyList == null)
            historyList = new ArrayList<>();
        if (histories == null) {
            histories = new History();
        }
        histories.setHistoryList(historyList);
//        对个数进行限制
        if (historyList.size() > mCurrentSize) {
            historyList = historyList.subList(0, mCurrentSize);
        }
        historyList.add(history);
//        保存记录
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }


    @Override
    public void doSearch(String keyword) {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        if (mKeyword == null || !mKeyword.equals(keyword)) {
            this.mKeyword = keyword;
            mCurrentPage= DEFAULT_PAGE;
            saveHistories(keyword);
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        doSearchOrMoreLoad(task);
    }

    private void doSearchOrMoreLoad(Call<SearchResult> task) {
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchResult> call, @NonNull Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "code =" + code);
                if (code == HttpURLConnection.HTTP_OK ||code ==HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    SearchResult result = response.body();
                    handleSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult> call, @NonNull Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        if (mCallback != null) {
            if (mCurrentPage == 1) {
                mCallback.onError();
            } else {
                mCurrentPage--;
                mCallback.onMoreLoadedError();
            }
        }
    }

    private void handleSuccess(SearchResult result) {
        if (mCallback != null) {
            if (isEmpty(result)) {
                if (mCurrentPage == 1) {
                    mCallback.onEmpty();
                } else {
                    mCallback.onMoreLoadedEmpty();
                }
            } else {
                if (mCurrentPage == 1) {
                    mCallback.onSearchSuccess(result);
                } else {
                    mCallback.onMoreLoaded(result);
                }
            }
        }
    }

    private boolean isEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    @Override
    public void research() {
        this.doSearch(mKeyword);
    }

    @Override
    public void loadedMore() {
        mCurrentPage++;
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mKeyword);
        doSearchOrMoreLoad(task);
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(@NonNull Call<SearchRecommend> call, @NonNull Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "code =" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchRecommend searchRecommend = response.body();
                    if (mCallback != null) {
                        if (searchRecommend != null) {
                            mCallback.onRecommendWordsLoaded(searchRecommend.getData());
                        } else {
                            mCallback.onError();
                        }
                    }
                } else {
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchRecommend> call, @NonNull Throwable t) {
                mCallback.onError();
            }
        });
    }

    @Override
    public void registerViewCallBack(ISearchCallBcak callback) {
        mCallback = callback;
    }

    @Override
    public void unRegisterViewCallBack(ISearchCallBcak callback) {
        mCallback = null;
    }
}
