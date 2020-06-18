package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.bean.Categories;
import com.example.taobaounion.presenter.interfaces.IHomePresenter;
import com.example.taobaounion.ui.activity.IMainActivity;
import com.example.taobaounion.ui.activity.ScanQrCodeActivity;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresentManger;
import com.example.taobaounion.view.IHomeCallBack;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallBack {

    private IHomePresenter mHomePresent;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;
    @BindView(R.id.home_pager)
    public ViewPager mViewPager;
    private HomePagerAdapter mHomePagerAdapter;
    @BindView(R.id.home_search)
    public EditText mSearch;
    @BindView(R.id.scan_icon)
    public ImageView mScanIcon;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initView(View view) {
//      选项卡绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base_home,container,false);
    }


    /**
     * 网络异常，点击事件 重新加载
     */
    @Override
    protected void onRetryClick() {
        if (mHomePresent != null) {
            mHomePresent.getCategories();
        }
    }

    /**
     * 释放资源
     */
    @Override
    protected void release() {
        if (mHomePresent != null) {
            mHomePresent.unRegisterViewCallBack(this);
        }

    }

    @Override
    protected void initListen() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if(activity instanceof IMainActivity)
                    ((IMainActivity) activity).switch2Search();
            }
        });
        mScanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScanQrCodeActivity.class));
            }
        });
    }

    /**
     * 初始化present
     */
    @Override
    protected void initPresent() {
        mHomePresent = PresentManger.getInstance().getHomePresent();
        mHomePresent.registerViewCallBack(this);
    }

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        mHomePresent.getCategories();
    }

    /**
     * @param categories ：UI得到的分类
     */
    @Override
    public void onCategories(Categories categories) {
        if (mHomePagerAdapter != null) {
            stateChange(State.SUCCESS);
            mHomePagerAdapter.setCategories(categories);
            LogUtils.d(this,"onCategories ...");
        }

    }

    @Override
    public void onLoading() {
        stateChange(State.LOADING);
    }

    @Override
    public void onError() {
        stateChange(State.ERROR);
    }

    @Override
    public void onEmpty() {
        stateChange(State.EMPTY);
    }
}
