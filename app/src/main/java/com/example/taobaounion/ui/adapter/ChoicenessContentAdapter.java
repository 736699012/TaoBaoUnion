package com.example.taobaounion.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.bean.ChoicenessContent;
import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoicenessContentAdapter extends RecyclerView.Adapter<ChoicenessContentAdapter.InnerHolder> {

    private List<ChoicenessContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> mData = new ArrayList<>();
    private OnItemClickListen mOnItemClickListen;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_right, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ChoicenessContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemBean = mData.get(position);
        holder.setData(itemBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListen != null) {
                    mOnItemClickListen.onItemClick(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(ChoicenessContent content) {
        if (content != null) {
            if (content.getCode() == Constant.SUCCESS_CODE) {
                List<ChoicenessContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean> uatm_tbk_item = content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item();
                mData.clear();
                mData.addAll(uatm_tbk_item);
                notifyDataSetChanged();
            }
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.right_choice_content_cover)
        public ImageView cover;
        @BindView(R.id.right_choice_content_offer_price)
        public TextView offerPrice;
        @BindView(R.id.right_choice_content_title)
        public TextView title;
        @BindView(R.id.choice_buy_bt)
        public TextView buyBtn;
        @BindView(R.id.right_choice_content_original_price)
        public TextView originalPrice;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ChoicenessContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean itemBean) {
            title.setText(itemBean.getTitle());
            Glide.with(cover.getContext()).load(itemBean.getPict_url()).into(cover);
            if (TextUtils.isEmpty(itemBean.getCoupon_click_url())) {

                buyBtn.setVisibility(View.GONE);
                originalPrice.setText("来晚了,没有优惠券");
            } else {
                buyBtn.setVisibility(View.VISIBLE);
                originalPrice.setText("原价:" + itemBean.getZk_final_price());
            }
            if(TextUtils.isEmpty(itemBean.getCoupon_info())){
                offerPrice.setVisibility(View.GONE);
            }else {
                offerPrice.setVisibility(View.VISIBLE);
                offerPrice.setText(itemBean.getCoupon_info());
            }
        }
    }
    public void setOnItemClickListen(OnItemClickListen onItemClickListen){
        mOnItemClickListen = onItemClickListen;
    }
    public interface OnItemClickListen{
        void onItemClick(IBaseInfo itemBean);
    }
}
