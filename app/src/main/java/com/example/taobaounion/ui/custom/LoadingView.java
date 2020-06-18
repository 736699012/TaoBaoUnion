package com.example.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class LoadingView extends AppCompatImageView {

    private int mRotate = 0;
    private boolean isRotate;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        isRotate = false;
    }

    public void startRotate() {
        isRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                mRotate += 10;
                if (mRotate >= 360) {
                    mRotate = 0;
                }
                invalidate();
//               判断是否还要旋转
                if (getVisibility() != VISIBLE && !isRotate) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 10);
                }

            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.rotate(mRotate, getWidth() / 2, getHeight() / 2);
        super.draw(canvas);
    }
}
