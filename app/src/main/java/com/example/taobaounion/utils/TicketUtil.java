package com.example.taobaounion.utils;

import android.content.Context;
import android.content.Intent;

import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.presenter.interfaces.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;

public class TicketUtil {

    public static void handle2TaoBao(Context context, IBaseInfo baseInfo){
        String cover = baseInfo.getPict_url();
        String title = baseInfo.getTitle();
        String url = baseInfo.getUrl();
        Intent intent = new Intent(context, TicketActivity.class);
        ITicketPresenter iTicketPresenter = PresentManger.getInstance().getTicketPresent();
        iTicketPresenter.getTicket(UrlUtil.getCoverUrl(url),title,cover);
        context.startActivity(intent);

    }
}
