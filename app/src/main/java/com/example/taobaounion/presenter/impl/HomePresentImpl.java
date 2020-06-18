package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.bean.Categories;
import com.example.taobaounion.presenter.interfaces.IHomePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManger;
import com.example.taobaounion.view.IHomeCallBack;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresentImpl implements IHomePresenter {

    private Retrofit mRetrofit;
    private IHomeCallBack mCallBack;

    @Override
    public void getCategories() {
        if (mCallBack != null) {
            mCallBack.onLoading();
        }

//        获取分类
        mRetrofit = RetrofitManger.getInstance().getRetrofit();
        Api api = mRetrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
//                数据结果
                int code = response.code();
                LogUtils.d(HomePresentImpl.this, "result code ==" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    if (mCallBack != null) {
                        Categories categories = response.body();
                        if (categories == null || categories.getData().size() == 0) {
                            mCallBack.onEmpty();
                        } else {
                            //  把拿到的值传给UI
                            LogUtils.i(HomePresentImpl.this, "body ==" + categories.toString());
                            mCallBack.onCategories(categories);
                        }


                    }
                } else {
                    if (mCallBack != null) {
                        mCallBack.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
//                加载失败时调用
                if (mCallBack != null) {
                    mCallBack.onError();
                }
            }
        });
    }

    @Override
    public void registerViewCallBack(IHomeCallBack iHomeCallBack) {
        this.mCallBack = iHomeCallBack;

    }

    @Override
    public void unRegisterViewCallBack(IHomeCallBack iHomeCallBack) {
        this.mCallBack = null;
    }
}
