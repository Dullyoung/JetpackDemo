package com.dullyoung.jetpackdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dullyoung.jetpackdemo.R;
import com.dullyoung.jetpackdemo.databinding.ViewBookReaderBinding;

/**
 * @author Dullyoung   2021/9/2
 */
public class BookReaderView extends FrameLayout {
    public BookReaderView(@NonNull Context context) {
        super(context);
        initViews(context);
    }

    public BookReaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public BookReaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public BookReaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private ViewBookReaderBinding mViewBookReaderBinding;

    private void initViews(Context context) {
        inflate(context, R.layout.view_book_reader, this);
        mViewBookReaderBinding = ViewBookReaderBinding.bind(this);
        mStartView = mViewBookReaderBinding.llStart;
        mMiddleView = mViewBookReaderBinding.llMiddle;
        mEndView = mViewBookReaderBinding.llEnd;

    }

    View mStartView;
    View mMiddleView;
    View mEndView;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //布局
        mStartView.layout(-getWidth(), 0, 0, getHeight());
        mMiddleView.layout(0, 0, getWidth(), getHeight());
        mEndView.layout(getWidth(), 0, 2 * getWidth(), getHeight());
        mStartView.setElevation(1);
        mEndView.setElevation(1);
        mMiddleView.setElevation(2);
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        resolveTouchEvent(event);
        return true;
    }


    /**
     * x初始量
     */
    float x = 0;
    /**
     * x轴偏移量
     */
    float xOffset = 0;
    /**
     * 正在显示的页面是三个页面中的第几个
     */
    int curPageIndex = 2;

    /**
     * @param event 触摸事件
     *              处理触摸事件
     */
    private void resolveTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                xOffset = event.getX() - x;
                relayout();
                break;
            case MotionEvent.ACTION_UP:
                onFinishMove();
                xOffset = 0;
                break;
            default:
                break;
        }
    }

    /**
     * 处理滑动中变化
     */
    private void relayout() {
        //只是按着 没动就不处理
        if (xOffset == 0) {
            return;
        }
        //手势右移 把上一页翻出来
        if (xOffset > 0) {
            View curView = getCurView(curPageIndex);
            View targetView = getTargetView(curPageIndex, false);
            View leftView = getTargetView(curPageIndex, true);
            //提高目标page的高度
            leftView.setElevation(1);
            curView.setElevation(1);
            targetView.setElevation(2);
            targetView.layout((int) (-getWidth() + xOffset), 0, (int) (xOffset), getHeight());
            return;
        }
        //xOffset<0 手势左移 把下一页拉出来
        View curView = getCurView(curPageIndex);
        View targetView = getTargetView(curPageIndex, true);
        View leftView = getTargetView(curPageIndex, false);
        leftView.setElevation(1);
        curView.setElevation(1);
        targetView.setElevation(2);
        int start = (int) (getWidth() + xOffset);
        int end = (int) (2 * getWidth() + xOffset);
        targetView.layout(start, 0, end, getHeight());
    }

    /**
     * @param index 当前索引 范围1-3
     * @return 当前正在展示的page
     */
    private View getCurView(@IntRange(from = 1, to = 3) int index) {
        if (index == 1) {
            return mStartView;
        } else if (curPageIndex == 2) {
            return mMiddleView;
        } else {
            return mEndView;
        }
    }

    /**
     * @param index  范围 1-3
     * @param toNext 翻下一页还是上一页
     * @return 返回目标view
     */
    private View getTargetView(@IntRange(from = 1, to = 3) int index, boolean toNext) {
        if (index == 1) {
            return toNext ? mMiddleView : mEndView;
        } else if (curPageIndex == 2) {
            return toNext ? mEndView : mStartView;
        } else {
            return toNext ? mStartView : mMiddleView;
        }
    }


    /**
     * 滑动结束
     */
    private void onFinishMove() {
        View curView = getCurView(curPageIndex);
        View nextView = getTargetView(curPageIndex, true);
        View lastView = getTargetView(curPageIndex, false);
        if (Math.abs(xOffset) < (int)(getWidth()/4)) {
            lastView.layout(-getWidth(), 0, 0, getHeight());
            curView.layout(0, 0, getWidth(), getHeight());
            nextView.layout(getWidth(), 0, 2 * getWidth(), getHeight());
            lastView.setElevation(1);
            curView.setElevation(2);
            nextView.setElevation(1);
            return;
        }
        //左边的翻出来
        if (xOffset > (int)(getWidth()/4)) {
            nextView.layout(-getWidth(), 0, 0, getHeight());
            lastView.layout(0, 0, getWidth(), getHeight());
            curView.layout(getWidth(), 0, 2 * getWidth(), getHeight());
            lastView.setElevation(2);
            curView.setElevation(1);
            nextView.setElevation(1);
            curPageIndex = (curPageIndex == 1) ? 3 : curPageIndex - 1;
            return;
        }

        //右边的翻出来
        if (xOffset < -(int)(getWidth()/4)) {
            curView.layout(-getWidth(), 0, 0, getHeight());
            nextView.layout(0, 0, getWidth(), getHeight());
            lastView.layout(getWidth(), 0, 2 * getWidth(), getHeight());
            lastView.setElevation(1);
            curView.setElevation(1);
            nextView.setElevation(2);
            curPageIndex = (curPageIndex == 3) ? 1 : curPageIndex + 1;
        }

    }

}
