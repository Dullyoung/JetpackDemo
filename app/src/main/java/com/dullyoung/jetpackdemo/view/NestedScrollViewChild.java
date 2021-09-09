package com.dullyoung.jetpackdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;

/**
 * @author Dullyoung   2021/9/8
 */
public class NestedScrollViewChild extends FrameLayout implements NestedScrollingChild {
    private String TAG = "aaaa";

    public NestedScrollViewChild(Context context) {
        super(context);
        init();
    }

    public NestedScrollViewChild(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollViewChild(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NestedScrollViewChild(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private NestedScrollingChildHelper mChildHelper;

    private void init() {
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return onTouchEvent(ev);
    }

    float dy = 0;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mView = getChildAt(0);
        mView.layout(0, 0, 0, mView.getMeasuredHeight());
    }

    View mView;
    float dx;
    float ly;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dy = event.getRawY();
                dx = event.getRawX();
                //开始竖向滚动 触发父view的onStartNestedScroll  父view要return true才能接着触发actionMove
                return startNestedScroll(SCROLL_AXIS_VERTICAL);
            case MotionEvent.ACTION_MOVE:
                float offsetX = event.getRawX() - dx;
                float cy = event.getRawY();
                float offsetY = cy - dy;
                //为了取每次新偏移量 而不是对于ACTION DOWN的总偏移量
                dy = cy;

                int[] consumed = new int[2];

                ly = mView.getTranslationY();
                //getHeight 返回的是已展示的高度  getMeasuredHeight返回控件总高度
                //偏移量最大就是总高度-可展示高度
                int curY = (int) Math.max(offsetY + ly, getHeight() - getMeasuredHeight());
                //最小是0 即不允许下拉 只允许上滑 上滑最多就总高度-可展示高度
                curY = (int) Math.min(0, curY);
                //消费掉的就是现在的Y和之前的Y的差值
                consumed[1] = (int) (curY - ly);
                //设置偏移 使内部视图图滚动
                mView.setTranslationY(curY);
                //消费的y就是layout前后的偏移量
                int unconsumedY = 0;
                //此时说明本view没有滚动了 那未消费的滚动就是本次产生的偏移量
                if (curY == ly) {
                    unconsumedY = (int) offsetY;
                }
                Log.i(TAG, "onTouchEvent: " + offsetY + "..." + curY + "///" + ly);
                //触发父view onNestedScroll回调
                dispatchNestedScroll(consumed[0], consumed[1], (int) offsetX,
                        unconsumedY, null);
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);

    }


}
