package com.dullyoung.jetpackdemo.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.dullyoung.jetpackdemo.R;

import java.util.Random;

/**
 * @author Dullyoung   2021/8/25
 */
public class PathMeasureView extends View {

    public PathMeasureView(Context context) {
        super(context);
        init();
    }

    public PathMeasureView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathMeasureView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PathMeasureView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mPaint;
    private Path roundPath;
    private Path mCurRoundPath;

    private Paint mTextPaint;

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setAntiAlias(true);

        mTextPaint=new Paint();
        mTextPaint.setTextSize(38);

        roundPath = new Path();
        mCurRoundPath = new Path();
        setBackgroundColor(0xffff9b27);
        roundPath.addCircle(500, 500, 300, Path.Direction.CW);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.iconaaaa);
        mMatrix = new Matrix();
        pathMeasure = new PathMeasure(roundPath, false);
        onFail();
    }


    float start = 0f;
    PathMeasure pathMeasure;

    public void loading() {
        success = false;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(animation -> {
            start = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        valueAnimator.start();

    }

    Bitmap mBitmap;
    Matrix mMatrix;

    private boolean isnext = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (success) {
            float endD = successPathMeasure.getLength() * successPercent * 2;
            //????????????????????????????????? ????????????50????????????????????? nextContour() ???????????????true ???????????????????????????
            if (successPercent > 0.5) {
                if (isnext) {
                    endD = successPathMeasure.getLength() * 2 * (successPercent - 0.5f);
                } else {
                    isnext = successPathMeasure.nextContour();
                    endD = successPathMeasure.getLength() * (successPercent - 0.5f);
                }
            }
            successPathMeasure.getSegment(0, endD, mCurSuccessPath, true);
            canvas.drawPath(mCurSuccessPath, mPaint);
            mCurRoundPath.reset();
            pathMeasure.getSegment(pathMeasure.getLength() * (1f - successPercent) / 2,
                    pathMeasure.getLength() * (successPercent + 1) / 2,
                    mCurRoundPath, true);
            canvas.drawPath(mCurRoundPath, mPaint);
            return;
        }
        mCurRoundPath.reset();
        float distance = pathMeasure.getLength() * start;
//        distance     start  disant -0
        float startF = (float) (distance - ((0.5 - Math.abs(start - 0.5)) * pathMeasure.getLength()));
//   ( distance-0.5*mLength)   start   distance
//mPath  1  dst  2
        pathMeasure.getSegment(startF, distance, mCurRoundPath, true);
        canvas.drawPath(mCurRoundPath, mPaint);

        //??????????????????
        float[] pos = new float[2];
        //???????????????????????????
        float[] tan = new float[2];

        pathMeasure.getPosTan(distance, pos, tan);
        //???????????????????????????????????????
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        mMatrix.reset();
        // ?????????????????????????????????
        mMatrix.postRotate(degree, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        // ??????????????????????????????????????????????????????
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);
        canvas.drawPath(roundPath, mPaint);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        canvas.drawText("?????????????????????",400,500,mTextPaint);

    }

    private boolean success;
    private Path mCurSuccessPath;
    private float successPercent;
    PathMeasure successPathMeasure;

    public void onFail() {
        Path successPath = new Path();
        mCurSuccessPath = new Path();
        successPath.moveTo(400, 400);
        successPath.lineTo(600, 600);
        successPath.moveTo(600, 400);
        successPath.lineTo(400, 600);
        successPathMeasure = new PathMeasure(successPath, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            successPercent = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        valueAnimator.start();
        isnext = false;
        success = true;
    }

    public void onSuccess() {

        Path successPath = new Path();
        mCurSuccessPath = new Path();
        successPath.moveTo(400, 500);
        successPath.lineTo(500, 600);
        successPath.lineTo(600, 300);
        successPathMeasure = new PathMeasure(successPath, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(animation -> {
            successPercent = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        valueAnimator.start();
        success = true;
    }

    private String TAG = "aaaa";

}
