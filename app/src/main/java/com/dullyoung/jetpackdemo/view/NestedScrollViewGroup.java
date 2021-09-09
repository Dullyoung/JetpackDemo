package com.dullyoung.jetpackdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;

/**
 * @author Dullyoung   2021/9/8
 */
public class NestedScrollViewGroup extends FrameLayout implements NestedScrollingParent {

    public NestedScrollViewGroup(Context context) {
        super(context);
    }

    public NestedScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public NestedScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child1 = getChildAt(0);
        View child2 = getChildAt(1);
        View child3 = getChildAt(2);
        //随意固定的常量值
        child1.layout(0, 0, getWidth(), 600);
        child2.layout(0, 600, getWidth(), 600 * 2);
        child3.layout(0, 600 * 2, getWidth(),
                600 * 2 + child3.getMeasuredHeight());
        maxY = 2340 - child3.getTop() - child3.getMeasuredHeight();
    }

    private String TAG = "aaaa";

    private boolean isNestedChild = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getChildByPosition(ev.getX(), ev.getY());
            y = ev.getY();
            if (view instanceof NestedScrollViewChild && view.isNestedScrollingEnabled()) {
                isNestedChild = true;
                return super.dispatchTouchEvent(ev);
            } else {
                translateY(ev);
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!isNestedChild) {
                translateY(ev);
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            isNestedChild = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    float y;
    float maxY;

    /**
     * @param ev 不是嵌套viewChild的时候自己处理 Action Move
     */
    void translateY(MotionEvent ev) {
        float offsetY = ev.getY() - y;
        float cy = offsetY + getTranslationY();
        cy = Math.max(cy, maxY);
        cy = Math.min(0, cy);
        setTranslationY(cy);
    }

    /**
     * @param x x
     * @param y y
     * @return 当前坐标view
     */
    private View getChildByPosition(float x, float y) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getTop() < y && view.getBottom() > y) {
                return view;
            }
        }
        return this;
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //把子view未消费的事件交由父view处理
        float ty = dyUnconsumed + getTranslationY();
        ty = Math.min(0, ty);
        View v3 = getChildAt(2);
        //不允许下拉 最多把第三个view拉出来
        ty = Math.max(v3.getMeasuredHeight() - 2340, ty);
        setTranslationY(ty);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //是嵌套子view纵向滑动的时候就开启嵌套滑动
        return child instanceof NestedScrollViewChild && nestedScrollAxes == SCROLL_AXIS_VERTICAL;
    }

}
