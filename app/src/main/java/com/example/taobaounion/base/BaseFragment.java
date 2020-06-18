package com.example.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {


    private View mErrorView;
    private View mEmptyView;
    private View mLoadingView;

    protected enum State {
        NONE, SUCCESS, ERROR, LOADING, EMPTY
    }

    private Unbinder mBind;
    private FrameLayout mFrameLayout;
    private View mSuccessView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = loadRootView(inflater,container);
        mFrameLayout = baseView.findViewById(R.id.base_container);
        loadStateView(inflater, container);
        mBind = ButterKnife.bind(this, baseView);
        initView(baseView);
        initListen();
        initPresent();
        loadData();
        return baseView;
    }

    /**
     * 监听事件的设置
     */
    protected void initListen() {

    }


    @OnClick(R.id.error_tips)
    public void onRetry(){
        LogUtils.d(this,"重试。。。");
        onRetryClick();
    }

    /**
     * 加载错误时，重新点击
     */
    protected void onRetryClick() {
    }


    /**
     * 加载底层的View
     * @param inflater :
     * @param container:
     * @return
     */
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base,container,false);
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        mSuccessView = loadSuccessView(inflater, container);
        mErrorView = loadErrorView(inflater, container);
        mEmptyView = loadEmptyView(inflater, container);
        mLoadingView = loadLoadingView(inflater, container);
        mFrameLayout.addView(mSuccessView);
        mFrameLayout.addView(mErrorView);
        mFrameLayout.addView(mEmptyView);
        mFrameLayout.addView(mLoadingView);
        stateChange(State.NONE);
    }

    /**
     * 通过当前状态来改变UI显示
     *
     * @param state :当前状态
     */
    protected void stateChange(State state) {
        State currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
    }

    /**
     * @param inflater   ：
     * @param container：
     * @return：加载成功的View
     */
    private View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getRootViewId(), container, false);
    }

    /**
     * @param inflater   ：
     * @param container：
     * @return：加载失败的View
     */
    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    /**
     * @param inflater   ：
     * @param container：
     * @return：加载空的View
     */

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    /**
     * @param inflater   ：
     * @param container：
     * @return：加载错误的View
     */
    private View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected void initView(View view) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
        mBind.unbind();
    }

    /**
     * 释放资源
     */
    protected void release() {

    }

    /**
     * 加载数据
     */
    protected void loadData() {

    }

    /**
     * 初始化控制器
     */
    protected void initPresent() {

    }

    protected abstract int getRootViewId();
}
