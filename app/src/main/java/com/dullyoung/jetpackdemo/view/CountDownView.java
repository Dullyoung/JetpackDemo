package com.dullyoung.jetpackdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Dullyoung   2021/8/24
 */
public class CountDownView extends View {
    public CountDownView(Context context) {
        super(context);
        initPaint();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }


    /**
     * 外部刻度的画笔
     */
    private Paint paint;

    /**
     * 红点的paint
     */
    private Paint pointPaint;

    /**
     * 中间文字的画笔
     */
    private Paint textPaint;

    /**
     * 中间白色圆的画笔
     */
    private Paint centerRound;
    /**
     * 红点的角度
     */
    private int pointAngle = 90;
    /**
     * 用来绘制原点用 绘制圆弧需要指定矩形
     */
    private RectF rect;
    /**
     * 中间的时间
     */
    private String time = "0:00";


    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        //控制圆环的宽度
        paint.setStrokeWidth(100);
        paint.setStrokeCap(Paint.Cap.BUTT);
        //float[] 奇数参数表示绘制的宽度 偶数表示间隔的宽度
        paint.setPathEffect(new DashPathEffect(new float[]{20f, 50f}, 0));

        centerRound = new Paint();
        centerRound.setColor(Color.WHITE);

        pointPaint = new Paint();
        pointPaint.setStrokeWidth(100);
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        rect = new RectF();

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(45);
        textPaint.setAntiAlias(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外圈刻度 为了能动态改变刻度宽度 就只能从0开始画刻度 而不是直接画圆
        rect.set(150, 150, getWidth() - 150, getHeight() - 150);
        for (int i = 0; i <= 360; i += 5) {
            if (Math.abs(i - pointAngle) < 30) {
                paint.setStrokeWidth(100 + 2 * (30 - Math.abs(i - pointAngle)));
            } else {
                paint.setStrokeWidth(100);
            }
            canvas.drawArc(rect, i, 1f, false, paint);
        }

        //画中心白色圆 因为改了StrokeWidth 刻度圆环内外都会有凸起 内部就再画个白色圆盖起来
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                getWidth() / 2f - 200, centerRound);
        //画红点
        rect.set(250, 250, getWidth() - 250, getHeight() - 250);
        canvas.drawArc(rect, pointAngle, 0.5f, false, pointPaint);

        //中间文字
        canvas.drawText(time, getWidth() / 2f, getHeight() / 2f, textPaint);
    }

    public void setPointAngle(float x, float y) {
        pointAngle = getHandlePosition(x, y);
        int sec = pointAngle / 6;
        if (sec == 60) {
            time = "1:00";
        } else {
            time = "0:" + (sec > 9 ? sec : "0" + sec);
        }
        postInvalidate();
    }

    private static final String TAG = "aaaaa";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: " + event.getX() + "y:" + event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setPointAngle(event.getX(), event.getY());
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * @param x x
     * @param y y
     * @return 根据触摸位置得到相对于圆环最近的坐标, 返回角度
     */
    private int getHandlePosition(float x, float y) {
        int centerPositionX = getWidth() / 2;
        int centerPositionY = getHeight() / 2;
        double radian = Math.atan(Math.abs(centerPositionY - y) / Math.abs(centerPositionX - x));
        // 区分象限
        int angle = (int) (radian * 180 / Math.PI);
        //第四象限不做处理
        if (y > centerPositionY && x <= centerPositionX) {
            //第三象限
            angle = 180 - angle;
        } else if (y <= centerPositionY && x > centerPositionX) {
            //第二象限
            angle = 360 - angle;
        } else if (y <= centerPositionY && x <= centerPositionX) {
            //第一象限
            angle += 180;
        }
        return angle;
    }
}
