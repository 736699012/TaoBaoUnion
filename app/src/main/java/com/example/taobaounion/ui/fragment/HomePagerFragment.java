package com.example.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.bean.Categories;
import com.example.taobaounion.model.bean.HomePagerContent;
import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.presenter.interfaces.ICategoryPagerPresenter;
import com.example.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.example.taobaounion.ui.adapter.HomePagerLooperAdapter;
import com.example.taobaounion.ui.custom.AutoLooperViewPager;
import com.example.taobaounion.utils.Constant;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresentManger;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.utils.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentAdapter.OnItemClickListen, HomePagerLooperAdapter.OnLooperItemClickListen {


    private ICategoryPagerPresenter mICategoryPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    @BindView(R.id.home_pager_looper)
    public AutoLooperViewPager mLooper;
    @BindView(R.id.home_pager_content_title)
    public TextView mTitle;
    @BindView(R.id.home_pager_points)
    public LinearLayout mLinearLayout;
    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;
    @BindView(R.id.home_pager_header)
    public LinearLayout homePagerHeader;
    @BindView(R.id.home_tb_nested_scroll_view)
    public TbNestedScrollView mTbNestedScrollView;
    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout mRefreshLayout;

    private HomePagerContentAdapter mHomePagerContentAdapter;
    private HomePagerLooperAdapter mHomePagerLooperAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean dataBean) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_HOME_PAGER_TITLE, dataBean.getTitle());
        bundle.putInt(Constant.KEY_HOME_PAGER_MATERIAL_ID, dataBean.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }


    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home_pager;
    }


    @Override
    public void onResume() {
        super.onResume();
        mLooper.startAutoLooper();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLooper.stopAutoLooper();
    }

    @Override
    protected void onRetryClick() {
        mICategoryPagerPresenter.reload(mMaterialId);
    }

    @Override
    protected void initView(View view) {
//        列表的布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));

//        创建适配器
        mHomePagerContentAdapter = new HomePagerContentAdapter();
//        设置适配器
        mContentList.setAdapter(mHomePagerContentAdapter);
        mHomePagerContentAdapter.setOnItemClickListen(this);
//        创建轮播图适配器
        mHomePagerLooperAdapter = new HomePagerLooperAdapter();
        mHomePagerLooperAdapter.setOnLooperItemClickListen(this);
//        设置适配器
        mLooper.setAdapter(mHomePagerLooperAdapter);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(true);
//        下拉加载更多
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mICategoryPagerPresenter != null) {
                    mICategoryPagerPresenter.loaderMore(mMaterialId);
                }
            }
        });
    }

    @Override
    protected void initListen() {
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int measuredHeight = 0;
                if (homePagerParent != null) {
                    measuredHeight = homePagerParent.getMeasuredHeight();
                }
                int headerMeasuredHeight = 0;
                if (homePagerHeader != null) {
                    headerMeasuredHeight = homePagerHeader.getMeasuredHeight();
                }
                if (mTbNestedScrollView != null) {
                    mTbNestedScrollView.setHeaderHeight(headerMeasuredHeight);
                }
                if (mContentList != null) {
                    ViewGroup.LayoutParams layoutParams = mContentList.getLayoutParams();
                    layoutParams.height = measuredHeight;
//                    mContentList.setLayoutParams(layoutParams);
                }

//                LogUtils.d(HomePagerFragment.this, "measuredHeight " + measuredHeight);

                if (measuredHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });

        mLooper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 被选中时触发
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                if (mHomePagerLooperAdapter.getDataSize() != 0) {
                    int realPosition = position % mHomePagerLooperAdapter.getDataSize();
//                更改point的颜色
                    updateLooperPoint(realPosition);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateLooperPoint(int realPosition) {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            View point = mLinearLayout.getChildAt(i);
            if (realPosition == i) {
                point.setBackgroundResource(R.drawable.shape_point_bg_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_point_bg_normal);
            }
        }
    }

    @Override
    protected void initPresent() {
        mICategoryPagerPresenter = PresentManger.getInstance().getCategoryPagerPresenter();
        mICategoryPagerPresenter.registerViewCallBack(this);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString(Constant.KEY_HOME_PAGER_TITLE);
            mMaterialId = bundle.getInt(Constant.KEY_HOME_PAGER_MATERIAL_ID);
//            LogUtils.d(this, "title == " + title);
//            LogUtils.d(this, "materialId == " + mMaterialId);
//            得到数据
            mICategoryPagerPresenter.getContentByCategoryId(mMaterialId);
            if (mTitle != null) {
                mTitle.setText(title);
            }
        }

    }

    @Override
    public void onContentLoad(List<HomePagerContent.DataBean> contents) {
        stateChange(State.SUCCESS);
        LogUtils.d(this, "onContentLoad ...");
        mHomePagerContentAdapter.setData(contents);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onError() {
        stateChange(State.ERROR);
    }

    @Override
    public void onEmpty() {
        stateChange(State.EMPTY);
    }

    @Override
    public void onLoading() {
        stateChange(State.LOADING);
    }

    @Override
    public void onLoaderMoreError() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("网络异常，加载错误");
    }

    @Override
    public void onLoaderMoreEmpty() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("没有新的商品了");
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        mHomePagerContentAdapter.addData(contents);
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("加载了" + contents.size() + "条数据");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        mHomePagerLooperAdapter.setData(contents);
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
//        把数据设置到中间
        if (mLooper != null) {
            mLooper.setCurrentItem(Integer.MAX_VALUE / 2 - dx);
        }
        if (mLinearLayout != null) {
            mLinearLayout.removeAllViews();
            int size = SizeUtils.dip2px(getContext(), 8);
            int marginLeft = SizeUtils.dip2px(getContext(), 5);
            for (int i = 0; i < contents.size(); i++) {
                View point = new View(getContext());
                if (i == 0) {
                    point.setBackgroundResource(R.drawable.shape_point_bg_selected);
                } else {
                    point.setBackgroundResource(R.drawable.shape_point_bg_normal);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                layoutParams.leftMargin = marginLeft;
                layoutParams.rightMargin = marginLeft;
                point.setLayoutParams(layoutParams);
                mLinearLayout.addView(point);
            }
        }


    }

    @Override
    public void onItemClickListen(IBaseInfo dataBean) {
        LogUtils.d(this,"onItemClickListen... " +dataBean.getTitle());
        handleChangeActivity(dataBean);
    }

    private void handleChangeActivity(IBaseInfo dataBean) {
        TicketUtil.handle2TaoBao(getContext(),dataBean);
    }

    @Override
    public void onLooperItemClickListen(IBaseInfo dataBean) {
        LogUtils.d(this,"onLooperItemClickListen... " +dataBean.getTitle());
        handleChangeActivity(dataBean);
    }
}
