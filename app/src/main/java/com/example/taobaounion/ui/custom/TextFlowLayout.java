package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {
    private List<String> mTextList = new ArrayList<>();

    private static final float DEFAULT_SIZE = 10.0f;
    private float mItemHorizontalSpace = DEFAULT_SIZE;
    private float mItemVerticalSpace = DEFAULT_SIZE;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowItemClickListen mOnFlowItemClickListen;

    public int getTextFlowSize(){
        return mTextList.size();
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mItemHorizontalSpace = ta.getDimension(R.styleable.TextFlowLayout_horizontalSpace, DEFAULT_SIZE);
        mItemVerticalSpace = ta.getDimension(R.styleable.TextFlowLayout_verticalSpace, DEFAULT_SIZE);
//        回收
        ta.recycle();
//        LogUtils.d(this, "mItemHorizontalSpace ==" + mItemHorizontalSpace);
//        LogUtils.d(this, "mItemVerticalSpace ==" + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        mTextList.clear();
        removeAllViews();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        LogUtils.d(this,"size ==" + mTextList.size());
        for (String text : mTextList) {
            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            view.setText(text);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFlowItemClickListen != null) {
                        mOnFlowItemClickListen.onFlowItemClick(text);
                    }
                }
            });
            addView(view);
        }
    }

    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount()==0)
            return;
//        LogUtils.d(this, "onMeasure ..." + getChildCount());
        lines.clear();
        List<View> line = null;
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec);
//        LogUtils.d(this, "mSelfWidth ..." + mSelfWidth);
//        测量孩子
        int childCount = getChildCount();
//        LogUtils.d(this,"childCount  " +childCount);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            child.getMeasuredWidth();
//            LogUtils.d(this, "child ..." + child.getMeasuredWidth());
            if (line == null) {
                line =createNewLine(child);
            } else {
                if (canBeAdd(child, line)) {
                    line.add(child);
                }else{
                    line = createNewLine(child);
                }
            }
        }
//        测量自己
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int mSelfHeight = (int) (lines.size() * getChildAt(0).getMeasuredHeight() +(lines.size()+1)*mItemHorizontalSpace);
        setMeasuredDimension(mSelfWidth,mSelfHeight);
    }

    private List<View> createNewLine(View child) {
        List<View> line = new ArrayList<>();
        line.add(child);
        lines.add(line);
        return line;
    }

    /**
     * 判断是否可以添加
     *
     * @param child
     * @param line
     */
    private boolean canBeAdd(View child, List<View> line) {
//        所有现存的View的宽度相加 + 间距 +  child的宽度
//        如果宽度小于等于parent的宽度，可以添加，否则在开一行
        int totalWidth = 0;
        for (View view : line) {
            totalWidth += view.getMeasuredWidth();
        }
        totalWidth += (line.size()+1) * mItemHorizontalSpace;
        totalWidth += child.getMeasuredWidth();
//        LogUtils.d(this, "totalWidth ..." + totalWidth);
        return totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        LogUtils.d(this, "onLayout ..." + getChildCount());
//        开始摆放位置
        int top = (int) mItemVerticalSpace;
        for (List<View> views : lines) {
            int left  = (int) mItemHorizontalSpace;
            for (View view : views) {
                view.layout(left,top,left + view.getMeasuredWidth(),top+view.getMeasuredHeight());
                left += view.getMeasuredWidth()+mItemHorizontalSpace;
            }
            top += mItemHeight + mItemVerticalSpace;
        }
    }

    public void setOnFlowItemClickListen(OnFlowItemClickListen onFlowItemClickListen){
        mOnFlowItemClickListen = onFlowItemClickListen;
    }

    public interface OnFlowItemClickListen{
        void onFlowItemClick(String text);
    }
}
