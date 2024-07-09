package com.dullyoung.jetpackdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GradientRectView extends View {
    public GradientRectView(Context context) {
        super(context);
        init();
    }

    public GradientRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    RectF rect;

    Paint gradientPaint;

    private void init() {
        rect = new RectF(0, 0, 1080, 900);

// 计算矩形的中心点
        float centerX = rect.centerX();
        float centerY = rect.centerY();

        Shader sweepGradient = new RadialGradient(centerX/2, centerY/2,270,
                Color.BLACK,Color.BLUE,Shader.TileMode.MIRROR);

// 创建一个带有渐变的画笔
        gradientPaint=new Paint();
        gradientPaint.setShader(sweepGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.set(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rect,100,100,gradientPaint);


    }
}
