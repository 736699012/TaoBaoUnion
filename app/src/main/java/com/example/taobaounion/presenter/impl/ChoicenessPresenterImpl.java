package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.bean.ChoicenessCategories;
import com.example.taobaounion.model.bean.ChoicenessContent;
import com.example.taobaounion.presenter.interfaces.IChoicenessPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManger;
import com.example.taobaounion.view.IChoicenessCallBack;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChoicenessPresenterImpl implements IChoicenessPresenter {


    private final Api mApi;
    private IChoicenessCallBack mCallback;

    public ChoicenessPresenterImpl() {
        Retrofit retrofit = RetrofitManger.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }


    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<ChoicenessCategories> task = mApi.getChoicenessCategories();
        task.enqueue(new Callback<ChoicenessCategories>() {
            @Override
            public void onResponse(Call<ChoicenessCategories> call, Response<ChoicenessCategories> response) {
                int code = response.code();
                LogUtils.d(ChoicenessPresenterImpl.this, "code " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    ChoicenessCategories categories = response.body();
                    if (mCallback != null) {
                        mCallback.onCategoriesLoad(categories);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<ChoicenessCategories> call, Throwable t) {
                onLoadError();
            }
        });
    }

    private void onLoadError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(ChoicenessCategories.DataBean dataBean) {
        Call<ChoicenessContent> task = mApi.getChoicenessContent(dataBean.getFavorites_id());
        task.enqueue(new Callback<ChoicenessContent>() {
            @Override
            public void onResponse(Call<ChoicenessContent> call, Response<ChoicenessContent> response) {
                int code = response.code();
                LogUtils.d(ChoicenessPresenterImpl.this, "code " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    ChoicenessContent content = response.body();
                    if (mCallback != null) {
                        mCallback.onContentLoad(content);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<ChoicenessContent> call, Throwable t) {
                LogUtils.d(ChoicenessPresenterImpl.this, t.toString());
                onLoadError();
            }
        });

    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerViewCallBack(IChoicenessCallBack callback) {
        this.mCallback = callback;
    }

    @Override
    public void unRegisterViewCallBack(IChoicenessCallBack callback) {
        this.mCallback = null;
    }
}
