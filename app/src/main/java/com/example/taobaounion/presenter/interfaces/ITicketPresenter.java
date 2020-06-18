package com.example.taobaounion.presenter.interfaces;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ITicketCallBack;

public interface ITicketPresenter extends IBasePresenter<ITicketCallBack> {

    void getTicket(String url,String title,String cover);
}
