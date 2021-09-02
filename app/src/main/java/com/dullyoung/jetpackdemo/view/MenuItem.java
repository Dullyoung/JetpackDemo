package com.dullyoung.jetpackdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @author Dullyoung   2021/8/27
 */
public class MenuItem extends LinearLayout {

    public MenuItem(Context context) {
        super(context);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("child count must be 2");
        }
        mMsgView = getChildAt(0);
        mMenuView = getChildAt(1);

    }

    private View mMsgView;
    private View mMenuView;
    private boolean showMenu = false;
    private boolean isMoving = false;
    private int xOffset = 0;
    private int menuWidth = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        menuWidth = mMenuView.getWidth();
        relayout();
    }


    private void relayout() {
        if (!isMoving) {
            //停止的时候 就两种状态 展开菜单 不展开菜单 直接固定值
            if (showMenu) {
                mMsgView.layout(-menuWidth, 0, getWidth() - menuWidth, getHeight());
                mMenuView.layout(getWidth() - menuWidth, 0, getWidth(), getHeight());
            } else {
                mMsgView.layout(0, 0, getWidth(), getHeight());
                mMenuView.layout(getWidth(), 0, getWidth() + menuWidth, getHeight());
            }

        } else {
            //手指从左往右 x是在增加的 所以 xOffset>0 这时候也需要视图右移
            //反之左移
            // 左侧消息View的左边界滑动范围就是 -menuWidth~0  menuWidth是右侧菜单宽度 这里图方便固定 完善的话就自己measureChild一下
            //             右边界 就是 宽度-menuWidth~宽度
            //右侧菜单 左边界 宽度-menuWidth -宽度 即左侧消息右边界
            //右边界  宽度+menuWidth~宽度
            int msgL = 0;
            int msgR = 0;
            //右移 要考虑之前的状态是开菜单还是关
            if (xOffset > 0) {
                if (showMenu) {
                    msgL = Math.min(-menuWidth + xOffset, 0);
                    msgR = getWidth() + msgL;
                } else {
                    msgR = getWidth();
                }
            } else if (xOffset < 0) {
                //左移
                if (showMenu) {
                    msgL = -menuWidth;
                    msgR = getWidth() - menuWidth;
                } else {
                    xOffset = Math.max(-menuWidth, xOffset);
                    msgL = xOffset;
                    msgR = getWidth() + msgL;
                }
            } else {//不动时
                msgL = showMenu ? -menuWidth : 0;
                msgR = showMenu ? getWidth() - menuWidth : getWidth();
            }
            mMsgView.layout(msgL, 0, msgR, getHeight());
            mMenuView.layout(msgR, 0,
                    menuWidth + msgR, getHeight());
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mMenuView != null && mMsgView != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
            resolveTouchEvent(ev);
        }
        return true;
    }

    float x = 0;

    private void resolveTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                x = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float cx = event.getX();
                xOffset = (int) (cx - x);
                relayout();
                break;
            case MotionEvent.ACTION_UP:
                if (xOffset > menuWidth / 3) {
                    showMenu = false;
                } else if (xOffset < menuWidth / 3) {
                    showMenu = true;
                }
                xOffset = 0;
                x = 0;
                isMoving = false;
                relayout();
                break;
            default:
                break;
        }
    }
}
