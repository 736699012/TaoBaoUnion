package com.example.taobaounion.ui.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.bean.History;
import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.model.bean.SearchRecommend;
import com.example.taobaounion.model.bean.SearchResult;
import com.example.taobaounion.presenter.interfaces.ISearchPresenter;
import com.example.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.KeyboardUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresentManger;
import com.example.taobaounion.utils.TicketUtil;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ISearchCallBcak;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchCallBcak, TextFlowLayout.OnFlowItemClickListen {

    private ISearchPresenter mSearchPresenter;
    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoryView;
    @BindView(R.id.search_recommend_keywords_view)
    public TextFlowLayout mRecommendView;
    @BindView(R.id.search_refresh)
    public TwinklingRefreshLayout mRefreshLayout;
    @BindView(R.id.history_view)
    public LinearLayout mHis;
    @BindView(R.id.recommend_view)
    public LinearLayout mRec;
    @BindView(R.id.search_history_delete)
    public ImageView mDelete;
    @BindView(R.id.search_result_view)
    public RecyclerView mSearchResult;
    private HomePagerContentAdapter mResultAdapter;
    @BindView(R.id.search_btn)
    public TextView mSearchBt;
    @BindView(R.id.search_value)
    public EditText mSearchValue;
    @BindView(R.id.search_remove)
    public ImageView mSearchRemove;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_search;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_header, container, false);
    }

    @Override
    protected void initPresent() {
        mSearchPresenter = PresentManger.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallBack(this);
        mSearchPresenter.getRecommendWords();
        mSearchPresenter.getHistories();
    }

    @Override
    protected void initListen() {

//        点击推荐词或者历史记录进行搜索
        mHistoryView.setOnFlowItemClickListen(this);
        mRecommendView.setOnFlowItemClickListen(this);

        //搜索事件
        mSearchBt.setOnClickListener(v -> {
            String string = mSearchBt.getText().toString();
            if ("取消".equals(string)) {
                //todo:
                switch2History();
            } else if ("搜索".equals(string)) {
                if (mSearchPresenter != null) {
                    toSearch(mSearchValue.getText().toString());
//                        mSearchPresenter.doSearch(mSearchValue.getText().toString());
                    KeyboardUtil.hideKeyboard(getContext(), v);
                }
            }
        });
        // 没有搜索时 ，显示历史记录和推荐
        mSearchRemove.setOnClickListener(v -> {
            mSearchValue.setText("");
            switch2History();
        });

        mSearchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 改变时触发
                mSearchRemove.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                mSearchBt.setText(hasInput(false) ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchValue.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                String key = v.getText().toString();
                if (!TextUtils.isEmpty(key)) {
                    toSearch(key);
//                        mSearchPresenter.doSearch(key);
                    return false;
                } else {
                    stateChange(State.EMPTY);
                }
            }
            return false;
        });

        mDelete.setOnClickListener(v -> mSearchPresenter.delHistories());
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loadedMore();
                }
            }
        });
        mResultAdapter.setOnItemClickListen(dataBean -> TicketUtil.handle2TaoBao(getContext(), dataBean));
    }

    private void switch2History() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
//        mHis.setVisibility(mHistoryView.getTextFlowSize() == 0 ? View.GONE : View.VISIBLE);
        mRec.setVisibility(mRecommendView.getTextFlowSize() == 0 ? View.GONE : View.VISIBLE);
        mRefreshLayout.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean isContainSpace) {
        if (isContainSpace) {
            int value = mSearchValue.getText().toString().length();
            return value != 0;
        } else {
            int value = mSearchValue.getText().toString().trim().length();
            return value != 0;
        }
    }

    @Override
    protected void onRetryClick() {
        if (mSearchPresenter != null) {
            mSearchPresenter.research();
        }
    }

    @Override
    protected void release() {
        mSearchPresenter.unRegisterViewCallBack(this);
    }

    @Override
    protected void initView(View view) {

        mSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultAdapter = new HomePagerContentAdapter();
        mSearchResult.setAdapter(mResultAdapter);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    public void onHistories(History histories) {
        stateChange(State.SUCCESS);
        if (histories == null || histories.getHistoryList().size() == 0) {
            mHis.setVisibility(View.GONE);
        } else {
            mHis.setVisibility(View.VISIBLE);
            mHistoryView.setTextList(histories.getHistoryList());
        }
    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        stateChange(State.SUCCESS);
        LogUtils.d(this, " result  " + result);
        mRec.setVisibility(View.GONE);
        mHis.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        try {
            mResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        } catch (Exception e) {
            stateChange(State.EMPTY);
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mRefreshLayout.finishLoadmore();
        ToastUtil.showToast("又来了一波宝贝");
        mResultAdapter.addData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
    }

    @Override
    public void onMoreLoadedError() {
        mRefreshLayout.finishLoadmore();
        ToastUtil.showToast("网络错误，你的宝贝飞走了");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshLayout.finishLoadmore();
        ToastUtil.showToast("没有你想要的宝贝了");
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        stateChange(State.SUCCESS);
        LogUtils.d(this, "recommend " + recommendWords);
        if (recommendWords.size() == 0) {
            mRec.setVisibility(View.GONE);
        } else {
            mRec.setVisibility(View.VISIBLE);
            List<String> keywords = new ArrayList<>();
            for (SearchRecommend.DataBean recommendWord : recommendWords) {
                keywords.add(recommendWord.getKeyword());
            }
            mRecommendView.setTextList(keywords);
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

    @Override
    public void onFlowItemClick(String text) {
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchValue.setText(text);
            mSearchResult.scrollToPosition(0);
            mSearchValue.requestFocus();
            mSearchValue.setSelection(text.length(),text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}
