package com.example.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.model.bean.Ticket;
import com.example.taobaounion.presenter.interfaces.ITicketPresenter;
import com.example.taobaounion.utils.Constant;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresentManger;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.view.ITicketCallBack;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketCallBack {


    private ITicketPresenter mTicketPresent;

    private boolean isHasTaoBao = false;
    @BindView(R.id.ticket_back_press)
    public ImageView mTicketBack;

    @BindView(R.id.ticket_cover)
    public ImageView mTicketCover;

    @BindView(R.id.ticket_tao_kou_ling)
    public TextView mTaoKouLing;
    @BindView(R.id.code_open_bt)
    public TextView mOpenBt;
    @BindView(R.id.ticket_error_text)
    public TextView mErrorText;
    @BindView(R.id.ticket_loading)
    public View mLoadingView;


    @Override
    protected void initPresent() {
        mTicketPresent = PresentManger.getInstance().getTicketPresent();
        mTicketPresent.registerViewCallBack(this);
        //判断是否安装有淘宝
        //act=android.intent.action.VIEW flg=0x4000000 hwFlg=0x10
        // pkg=com.taobao.taobao  包名
        // cmp=com.taobao.taobao/com.taobao.tao.TBMainActivity (has extras)}
        // from uid 10131
//        检查是否有淘宝
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(Constant.TAOBAO_PACKEDG, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            isHasTaoBao = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isHasTaoBao = false;
        }
        LogUtils.d(this, "isHasTaoBao " + isHasTaoBao);
        mOpenBt.setText(isHasTaoBao ? "打开淘宝" : "复制淘口令");
    }

    @Override
    protected void recycle() {
        if (mTicketPresent != null) {
            mTicketPresent.unRegisterViewCallBack(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        mTicketBack.setOnClickListener(v -> finish());
        mOpenBt.setOnClickListener(v -> {
//                复制口令
            String tao = mTaoKouLing.getText().toString().trim();
            LogUtils.d(TicketActivity.this,tao);
            ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data =ClipData.newPlainText("luffy_taobao_ticket",tao);
            cbm.setPrimaryClip(data);
            if(isHasTaoBao){
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
                intent.setComponent(componentName);
//                    跳转页面
                startActivity(intent);
            }else{
                ToastUtil.showToast("复制口令,打开淘宝");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicket(Ticket ticket, String cover) {

        if (mTicketCover != null && !TextUtils.isEmpty(cover)) {

            Glide.with(this).load(cover).into(mTicketCover);
        }
        if(TextUtils.isEmpty(cover)){
            mTicketCover.setImageResource(R.mipmap.no_image);
        }
        if (mTaoKouLing != null && ticket != null && ticket.getData() != null && ticket.getData().getTbk_tpwd_create_response() != null) {

            mTaoKouLing.setText(ticket.getData().getTbk_tpwd_create_response().getData().getModel());
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoading() {
        if (mErrorText != null) {
            mErrorText.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError() {
        if (mErrorText != null) {
            mErrorText.setVisibility(View.VISIBLE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onEmpty() {

    }
}
