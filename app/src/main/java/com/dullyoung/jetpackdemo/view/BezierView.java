package com.dullyoung.jetpackdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Dullyoung   2021/8/26
 */
public class BezierView extends View {
    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Paint mPaint;
    Path mPath;

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setDither(true);
        mPath = new Path();
        point = new Point(0, 100);
        point2 = new Point(50, 0);
        point3 = new Point(100, 100);
    }

    Point point, point2, point3;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //绘制锯齿边
//        for (int i = 0; i < getWidth(); i += 100) {
//            mPath.moveTo(point.x + i, point.y);
//            mPath.quadTo(point2.x + i, point2.y, point3.x + i, point3.y);
//        }
        mPath.moveTo(point.x, point.y);
        mPath.quadTo(point2.x , point2.y, point3.x, point3.y);
        canvas.drawPath(mPath, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawPoint(point.x, point.y, mPaint);
        canvas.drawPoint(point2.x, point2.y, mPaint);
        canvas.drawPoint(point3.x, point3.y, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            point2.x = (int) event.getX();
            point2.y = (int) event.getY();
            postInvalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }
}
