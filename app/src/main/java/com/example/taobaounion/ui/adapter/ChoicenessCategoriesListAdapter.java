package com.example.taobaounion.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.model.bean.ChoicenessCategories;

import java.util.ArrayList;
import java.util.List;

public class ChoicenessCategoriesListAdapter extends RecyclerView.Adapter<ChoicenessCategoriesListAdapter.InnerHolder> {
    private List<ChoicenessCategories.DataBean> mData = new ArrayList<>();
    private int mSelected = 0;
    private OnLeftItemClickListen mOnLeftItemClickListen ;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_left, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.left_category_text);
        textView.setText(mData.get(position).getFavorites_title());
        if (mSelected == position) {
            textView.setBackground(textView.getResources().getDrawable(R.color.color_EFEEEE, null));
        } else {
            textView.setBackground(textView.getResources().getDrawable(R.color.white, null));
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnLeftItemClickListen != null && mSelected != position) {
                mSelected = position;
                mOnLeftItemClickListen.onLeftItemClick(mData.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<ChoicenessCategories.DataBean> data) {
        mData.clear();
        this.mData = data;
        notifyDataSetChanged();
        if (mOnLeftItemClickListen != null) {
            mOnLeftItemClickListen.onLeftItemClick(mData.get(mSelected));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftItemClickListen(OnLeftItemClickListen onLeftItemClickListen) {
        mOnLeftItemClickListen = onLeftItemClickListen;
    }

    public interface OnLeftItemClickListen {
        void onLeftItemClick(ChoicenessCategories.DataBean dataBean);
    }
}
