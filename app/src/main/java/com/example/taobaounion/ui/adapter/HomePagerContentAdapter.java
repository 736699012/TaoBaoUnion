package com.example.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.model.bean.ILinearInfo;
import com.example.taobaounion.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    private List<ILinearInfo> mDataBeans = new ArrayList<>();
    private OnItemClickListen mOnItemClickListen;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
//        LogUtils.d(this,"onCreateViewHolder...");
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ILinearInfo dataBean = mDataBeans.get(position);
//        LogUtils.d(this,"onBindViewHolder..."+position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListen != null) {
                mOnItemClickListen.onItemClickListen(dataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeans.size();
    }

    public void setData(List<? extends ILinearInfo> contents) {
        mDataBeans.clear();
        mDataBeans.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearInfo> contents) {
        int begin = mDataBeans.size();
        mDataBeans.addAll(contents);
        notifyItemRangeChanged(begin, contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView mImageView;

        @BindView(R.id.goods_title)
        public TextView mTitle;

        @BindView(R.id.goods_offer_prise)
        public TextView mOfferPrise;
        @BindView(R.id.goods_after_offer_prise)
        public TextView mFinalPrise;
        @BindView(R.id.goods_before_offer_prise)
        public TextView mBeforePrise;
        @BindView(R.id.goods_sell_count)
        public TextView mSellCount;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ILinearInfo dataBean) {
            Context context = itemView.getContext();
            mTitle.setText(dataBean.getTitle());
            String finalPrice = dataBean.getZk_final_price();
            long couponAmount = dataBean.getCoupon_amount();
            mOfferPrise.setText(context.getString(R.string.text_offer_prise, couponAmount));
            ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();
            int height = layoutParams.height;
            int width = layoutParams.width;
            int size = (width > height ? width : height) / 2;
            Glide.with(context).load(UrlUtil.getCoverUrl(dataBean.getPict_url(), size)).into(mImageView);
            mBeforePrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mBeforePrise.setText(finalPrice);
            float resultPrice = Float.parseFloat(finalPrice) - couponAmount;
            mFinalPrise.setText(context.getString(R.string.text_goods_before_offer_price, String.format("%.2f", resultPrice)));
            mSellCount.setText(context.getString(R.string.text_goods_sell_count, dataBean.getVolume()));
        }
    }

    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        mOnItemClickListen = onItemClickListen;
    }

    public interface OnItemClickListen {
        void onItemClickListen(IBaseInfo dataBean);
    }
}
