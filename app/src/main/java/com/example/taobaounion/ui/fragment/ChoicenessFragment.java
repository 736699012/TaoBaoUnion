package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.bean.ChoicenessCategories;
import com.example.taobaounion.model.bean.ChoicenessContent;
import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.presenter.interfaces.IChoicenessPresenter;
import com.example.taobaounion.ui.adapter.ChoicenessCategoriesListAdapter;
import com.example.taobaounion.ui.adapter.ChoicenessContentAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresentManger;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.view.IChoicenessCallBack;

import butterknife.BindView;

public class ChoicenessFragment extends BaseFragment implements IChoicenessCallBack, ChoicenessCategoriesListAdapter.OnLeftItemClickListen, ChoicenessContentAdapter.OnItemClickListen {

    private IChoicenessPresenter mChoicenessPresenter;
    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;
    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;
    private ChoicenessCategoriesListAdapter mCategoriesListAdapter;
    private ChoicenessContentAdapter mContentAdapter;
    @BindView(R.id.header_title)
    public TextView headerTitle;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_choiceness;
    }

    @Override
    protected void initView(View view) {
        stateChange(State.SUCCESS);
        headerTitle.setText(R.string.text_choice_title);
        mCategoriesListAdapter = new ChoicenessCategoriesListAdapter();
        //            设置布局管理
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        //            设置适配器
        leftCategoryList.setAdapter(mCategoriesListAdapter);
        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentAdapter = new ChoicenessContentAdapter();
        rightContentList.setAdapter(mContentAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topOrBottom = SizeUtils.dip2px(getContext(), 4);
                int leftOrRight = SizeUtils.dip2px(getContext(), 6);
                outRect.top = topOrBottom;
                outRect.bottom = topOrBottom;
                outRect.right = leftOrRight;
                outRect.left = leftOrRight;
            }
        });
    }

    @Override
    protected void initPresent() {
        mChoicenessPresenter = PresentManger.getInstance().getChoicenessPresenter();
        mChoicenessPresenter.registerViewCallBack(this);
        mChoicenessPresenter.getCategories();
    }

    @Override
    protected void initListen() {
        mCategoriesListAdapter.setOnLeftItemClickListen(this);
        mContentAdapter.setOnItemClickListen(this);
    }

    @Override
    protected void release() {
        mChoicenessPresenter.unRegisterViewCallBack(this);
    }

    @Override
    public void onCategoriesLoad(ChoicenessCategories categories) {
        stateChange(State.SUCCESS);
        LogUtils.d(this, "category  ==" + categories.toString());
//        mChoicenessPresenter.getContentByCategory(categories.getData().get(0));
        if (leftCategoryList != null) {
            mCategoriesListAdapter.setData(categories.getData());
        }
    }

    @Override
    protected void onRetryClick() {
        if (mChoicenessPresenter != null) {
            mChoicenessPresenter.reloadContent();
        }
    }

    @Override
    public void onContentLoad(ChoicenessContent content) {
        mContentAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_bar_header,container,false);
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

    }

    @Override
    public void onLeftItemClick(ChoicenessCategories.DataBean dataBean) {
        LogUtils.d(this, "title  " + dataBean.getFavorites_title());
        if (mChoicenessPresenter != null) {
            mChoicenessPresenter.getContentByCategory(dataBean);
        }
    }

    @Override
    public void onItemClick(IBaseInfo itemBean) {
        TicketUtil.handle2TaoBao(getContext(),itemBean);
    }
}
