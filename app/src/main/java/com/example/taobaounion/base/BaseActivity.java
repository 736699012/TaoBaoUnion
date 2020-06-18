package com.example.taobaounion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
//        初始化界面
        initView();
//        初始化事件
        initEvent();
//        初始化present
        initPresent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycle();
    }

    protected void recycle() {

    }

    protected abstract void initPresent();

    protected void initEvent() {

    }

    protected abstract void initView();

    protected abstract int getLayoutId();
}
