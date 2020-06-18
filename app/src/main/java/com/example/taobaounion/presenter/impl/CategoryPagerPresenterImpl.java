package com.example.taobaounion.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.bean.HomePagerContent;
import com.example.taobaounion.presenter.interfaces.ICategoryPagerPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManger;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {


    private Map<Integer, Integer> pageInfo = new HashMap<>();
    private List<ICategoryPagerCallback> mCallbacks = new ArrayList<>();
    private final int DEFAULT_PAGE = 1;
    private Integer mTargetPage;


    @Override
    public void getContentByCategoryId(int categoryId) {

        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        Call<HomePagerContent> task = createTask(categoryId);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "code ==" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent content = response.body();
                    LogUtils.d(CategoryPagerPresenterImpl.this, "body == " + content.toString());
                    handleHomePagerContentResult(content, categoryId);
                } else {
                    handleNetWorkError(categoryId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, "onFailure ==" + t.toString());
                handleNetWorkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId) {
        Retrofit mRetrofit = RetrofitManger.getInstance().getRetrofit();
        Api api = mRetrofit.create(Api.class);
        mTargetPage = pageInfo.get(categoryId);
        if (mTargetPage == null) {
            mTargetPage = DEFAULT_PAGE;
            pageInfo.put(categoryId, mTargetPage);
        } else {
            mTargetPage++;
            pageInfo.put(categoryId, mTargetPage);
        }
//        LogUtils.d(this,"page == " +mTargetPage);
        return api.getHomePagerContent(categoryId, mTargetPage);
    }

    private void handleNetWorkError(int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }

    }

    private void handleHomePagerContentResult(HomePagerContent content, int categoryId) {
        List<HomePagerContent.DataBean> data = content.getData();
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (content.getData().size() != 0) {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoad(data);
                    LogUtils.d(this, "handleHomePagerContentResult  onContentLoad...");
                } else {
                    callback.onEmpty();
                    LogUtils.d(this, "handleHomePagerContentResult  onEmpty...");
                }
            }
        }

    }

    @Override
    public void loaderMore(int categoryId) {
        Call<HomePagerContent> task = createTask(categoryId);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
//                成功
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "loaderMore code  " + code);
                HomePagerContent content = response.body();
                if (code == HttpURLConnection.HTTP_OK||code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    handleMoreResult(categoryId, content);
                } else{
                    handleMoreNetError(categoryId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, t.toString());
//                失败
                handleMoreNetError(categoryId);
            }
        });
    }

    /**
     * 加载更多失败
     *
     * @param categoryId :
     */
    private void handleMoreNetError(int categoryId) {
        mTargetPage--;
        pageInfo.put(categoryId, mTargetPage);
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError();
            }
        }
    }

    /**
     * 加载更多结果
     *
     * @param categoryId
     * @param content
     */
    private void handleMoreResult(int categoryId, HomePagerContent content) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (content == null || content.getData().size() == 0) {
                    callback.onLoaderMoreEmpty();
                } else {
                    callback.onLoaderMoreLoaded(content.getData());
                }
            }
        }
    }

    @Override
    public void reload(int categoryId) {
        getContentByCategoryId(categoryId);
    }


    @Override
    public void registerViewCallBack(ICategoryPagerCallback iHomeCallBack) {
        if (!mCallbacks.contains(iHomeCallBack)) {
            mCallbacks.add(iHomeCallBack);
        }
    }

    @Override
    public void unRegisterViewCallBack(ICategoryPagerCallback iHomeCallBack) {
        mCallbacks.remove(iHomeCallBack);
    }


}
