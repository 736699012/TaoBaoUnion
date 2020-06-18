package com.example.taobaounion.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.bean.Ticket;
import com.example.taobaounion.model.bean.TicketParams;
import com.example.taobaounion.presenter.interfaces.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManger;
import com.example.taobaounion.utils.UrlUtil;
import com.example.taobaounion.view.ITicketCallBack;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresentImpl implements ITicketPresenter {

    private ITicketCallBack mITicketCallBack;
    private Ticket mTicket;
    private String mCover;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;


    @Override
    public void getTicket(String url, String title, String cover) {
        LogUtils.d(this,"url == "+ url);
//        LogUtils.d(this,"cover == "+ cover);
//        LogUtils.d(this,"title == "+ title);
        mCover = UrlUtil.getCoverUrl(cover);
        onLoading();
        Retrofit retrofit = RetrofitManger.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(UrlUtil.getTicketUrl(url), title);
        Call<Ticket> task = api.getTicketResult(ticketParams);
        task.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(@NonNull Call<Ticket> call, @NonNull Response<Ticket> response) {
                int code = response.code();
                LogUtils.d(TicketPresentImpl.this, "ticket result code " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    mTicket = response.body();
                    LogUtils.d(TicketPresentImpl.this, "body " + mTicket);
                    onLoadSuccess();
                } else {
                    onLoadError();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Ticket> call, @NonNull Throwable t) {
                LogUtils.d(TicketPresentImpl.this, "onFailure ...");
                onLoadError();

            }
        });
    }

    private void onLoading() {
        if (mITicketCallBack != null) {
            mITicketCallBack.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    private void onLoadError() {
        if (mITicketCallBack != null) {
            mITicketCallBack.onError();
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    private void onLoadSuccess() {
        if (mITicketCallBack != null) {
            mITicketCallBack.onTicket(mTicket, mCover);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    @Override
    public void registerViewCallBack(ITicketCallBack callback) {
        mITicketCallBack = callback;
        if(mCurrentState !=LoadState.NONE){
            switch (mCurrentState){
                case SUCCESS:
                    mITicketCallBack.onTicket(mTicket,mCover);
                    break;
                case LOADING:
                    mITicketCallBack.onLoading();
                    break;
                case ERROR:
                    mITicketCallBack.onError();
                    break;
            }
        }

    }

    @Override
    public void unRegisterViewCallBack(ITicketCallBack callback) {
        mITicketCallBack = null;
    }
}
