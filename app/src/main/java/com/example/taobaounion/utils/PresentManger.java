package com.example.taobaounion.utils;

import com.example.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.example.taobaounion.presenter.impl.ChoicenessPresenterImpl;
import com.example.taobaounion.presenter.impl.HomePresentImpl;
import com.example.taobaounion.presenter.impl.OnSellPresenterImpl;
import com.example.taobaounion.presenter.impl.SearchPresenterImpl;
import com.example.taobaounion.presenter.impl.TicketPresentImpl;
import com.example.taobaounion.presenter.interfaces.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.interfaces.IChoicenessPresenter;
import com.example.taobaounion.presenter.interfaces.IHomePresenter;
import com.example.taobaounion.presenter.interfaces.IOnSellPresenter;
import com.example.taobaounion.presenter.interfaces.ISearchPresenter;
import com.example.taobaounion.presenter.interfaces.ITicketPresenter;

public class PresentManger {
    private static final PresentManger ourInstance = new PresentManger();
    private final IHomePresenter mHomePresent;
    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final ITicketPresenter mTicketPresent;
    private final IChoicenessPresenter mChoicenessPresenter;
    private final IOnSellPresenter mOnSellPresenter;
    private final ISearchPresenter mSearchPresenter;

    public static PresentManger getInstance() {
        return ourInstance;
    }

    public IHomePresenter getHomePresent() {
        return mHomePresent;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public ITicketPresenter getTicketPresent() {
        return mTicketPresent;
    }

    public IChoicenessPresenter getChoicenessPresenter() {
        return mChoicenessPresenter;
    }

    public IOnSellPresenter getOnSellPresenter() {
        return mOnSellPresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }

    private PresentManger() {
        mHomePresent = new HomePresentImpl();
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mTicketPresent = new TicketPresentImpl();
        mChoicenessPresenter = new ChoicenessPresenterImpl();
        mOnSellPresenter = new OnSellPresenterImpl();
        mSearchPresenter = new SearchPresenterImpl();
    }
}
