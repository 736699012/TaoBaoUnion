package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;

public class AutoLooperViewPager extends ViewPager {

    public static final long DEFAULT_DURATION = 3000;
    private long mDuration =DEFAULT_DURATION;
    public AutoLooperViewPager(@NonNull Context context) {
        this(context,null);
    }

    public AutoLooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLooperStyle);

        mDuration = typedArray.getInteger(R.styleable.AutoLooperStyle_duration, (int) DEFAULT_DURATION);
//        回收
        typedArray.recycle();
    }

    public void startAutoLooper() {
        isLooper = true;
        post(mTask);
    }

    public void setDuration(long duration){
        this.mDuration = duration;
    }

    private boolean isLooper = false;
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            setCurrentItem(++currentItem, false);
//            LogUtils.d(AutoLooperViewPager.this, "looping...");
            if (isLooper) {
                postDelayed(this, mDuration);
            }

        }
    };

    public void stopAutoLooper() {
        isLooper = false;
        removeCallbacks(mTask);
    }
}
