package com.dullyoung.modulea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @Author Dullyoung
 * @Date 2022-06-14
 * @Desp
 */
public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Paint mPaint;

    private void init() {
        mPaint = new Paint();

        mPath = new Path();
        mBgPt = new Paint();
        resetPaint();
        setGradientPaint();
    }

    private void resetPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(50);
        mPaint.setColor(Color.MAGENTA);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    private void setGradientPaint() {
       mPaint.reset();
       mPaint.setAntiAlias(true);
       mPaint.setStrokeWidth(50);
       mPaint.setShader(new LinearGradient(0, 0,
                1080, 2000,
                Color.WHITE, Color.BLUE,
                Shader.TileMode.CLAMP));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    Paint mBgPt;

    Path mPath;

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {


        mPath.moveTo(0, 0);
        mPath.lineTo(0, 300);
        mPath.lineTo(400, 50);
        mPath.lineTo(560, 1600);
        mPath.lineTo(800, 800);
        mPath.lineTo(1000, 1500);
        mPath.lineTo(1080, 0);
        mPath.moveTo(1080, 2000);
        setGradientPaint();
        canvas.drawPath(mPath, mPaint);
        resetPaint();
        //xFer mode  DST down level,  SRC up level
        canvas.drawPath(mPath, mPaint);
    }
}
