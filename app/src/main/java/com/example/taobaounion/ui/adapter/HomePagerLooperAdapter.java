package com.example.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.taobaounion.model.bean.HomePagerContent;
import com.example.taobaounion.model.bean.IBaseInfo;
import com.example.taobaounion.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class HomePagerLooperAdapter extends PagerAdapter {

    List<HomePagerContent.DataBean> mDataBeans = new ArrayList<>();
    private OnLooperItemClickListen mOnLooperItemClickListen;

    public int getDataSize() {
        return mDataBeans.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % mDataBeans.size();
        ImageView imageView = new ImageView(container.getContext());
        HomePagerContent.DataBean data = mDataBeans.get(realPosition);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(v -> mOnLooperItemClickListen.onLooperItemClickListen(mDataBeans.get(realPosition)));
        int height = container.getMeasuredHeight();
        int width = container.getMeasuredWidth();
        int size = (width > height ? width : height) / 2;
        Glide.with(container.getContext()).load(Constant.IMG_BASE_URL + data.getPict_url()+"_"+size+"x"+size+".jpg").into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mDataBeans.clear();
        mDataBeans.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperItemClickListen(OnLooperItemClickListen onLooperItemClickListen){
        mOnLooperItemClickListen = onLooperItemClickListen;

    }

    public interface OnLooperItemClickListen{
        void onLooperItemClickListen(IBaseInfo dataBean);
    }
}
