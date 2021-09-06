package com.dullyoung.jetpackdemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;

import com.dullyoung.jetpackdemo.R;

/**
 * surfaceView 非UI线程绘制示例
 *
 * @author Dullyoung   2021/9/6
 */
public class FireworkView extends SurfaceView implements SurfaceHolder.Callback {
    public FireworkView(Context context) {
        super(context);
        init();
    }

    public FireworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FireworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FireworkView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private final static String TAG = "FireworkView";
    private DrawRunnable mDrawRunnable;

    /**
     * 是否正在绘制引线
     */
    private boolean isDrawingLauncher = true;

    /**
     * 引线画笔
     */
    private Paint mLauncherPaint;

    /**
     * 烟花所在的区域
     */
    private Rect mFireworkRect;

    /**
     * 烟花画笔
     */
    private Paint mFireworkPaint;

    private Bitmap mFireworkBitmap;


    private Path mLauncherCurPath;
    private float mLauncherPercent;
    private PathMeasure mLauncherPm;


    private float fireworkScale;
    private float fireworkAlpha = 1;

    private int height = 0;
    private int width = 0;

    /**
     * surfaceView虽然是继承的view 但是并没有重写{@link View#onDraw(Canvas)}
     * 所以我们要自己来拿到画布自己画
     *
     * @param canvas 画布
     */
    private void doDraw(Canvas canvas) {
        //因为烟花图片没有搞到png的 带有黑色背景 干脆就整个画布化成黑的就是伪"png"了！！！
        canvas.drawColor(Color.BLACK);
        if (isDrawingLauncher) {
            //绘制引线 从下往上的动画
            mLauncherCurPath.reset();
            float distance = mLauncherPm.getLength() * mLauncherPercent + 30;
            float startF = mLauncherPercent * mLauncherPm.getLength();
            //获取path片段
            mLauncherPm.getSegment(startF, distance, mLauncherCurPath, true);
            canvas.drawPath(mLauncherCurPath, mLauncherPaint);
        } else {
            mFireworkPaint.setAlpha((int) (fireworkAlpha * 255));
            canvas.drawBitmap(mFireworkBitmap, null, mFireworkRect, mFireworkPaint);
        }
    }

    private void init() {
        //初始化sh
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setKeepScreenOn(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    private void initPaint() {
        mLauncherPaint = new Paint();
        mLauncherPaint.setColor(0xffc37e00);
        mLauncherPaint.setStrokeCap(Paint.Cap.ROUND);
        mLauncherPaint.setStyle(Paint.Style.STROKE);
        mLauncherPaint.setStrokeWidth(10);

        mFireworkRect = new Rect();
        mFireworkPaint = new Paint();
        mFireworkBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.yh);
        Path launcherFullPath = new Path();
        //让他从左下到烟花中心
        launcherFullPath.moveTo(0, height);
        //x1 y1 是控制点  后面y2的300是盲猜的烟花爆炸中心
        launcherFullPath.quadTo(0, 500 + width / 4f, width / 2f, 300 + width / 4f);
        mLauncherCurPath = new Path();
        mLauncherPm = new PathMeasure(launcherFullPath, false);
    }

    /**
     * 为了只初始化一次。surfaceView重新回到前台就会触发
     * {@link SurfaceHolder.Callback#surfaceChanged(SurfaceHolder, int, int, int)}
     * 方法，因为是在这里开始动画的 这里不拦截就会有多个烟花同时绘制
     * 随便取个变量拦截 也可以加个boolean变量判断
     */

    public void startAnim() {
        if (mLauncherPm != null) {
            return;
        }
        initPaint();
        startLauncherAnim();
    }


    /**
     * 引线的动画  进度百分比
     */
    private void startLauncherAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.addUpdateListener(animation -> {
            isDrawingLauncher = true;
            mLauncherPercent = (float) animation.getAnimatedValue();
            mDrawRunnable.run();
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startDrawFirework();
            }
        });
    }

    /**
     * 绘制爆炸效果
     * 这里没有用矩阵。
     * 就动态的改变矩形的大小达到缩放的目的
     */
    private void startDrawFirework() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.addUpdateListener(animation -> {
            isDrawingLauncher = false;
            fireworkAlpha = 1;
            fireworkScale = (float) animation.getAnimatedValue();
            //左位于四分之一处  右位于四分之三处   宽度为一半总宽度的正方形
            int centerX = width / 2;
            int centerY = centerX + 50;
            //以中心点变化
            mFireworkRect.set((int) (centerX - width * fireworkScale / 2 / 2),
                    (int) (centerY - width * fireworkScale / 2 / 2),
                    (int) (centerX + width * fireworkScale / 2 / 2),
                    (int) (centerY + width * fireworkScale / 2 / 2));
            mDrawRunnable.run();
        });
        valueAnimator.setDuration(500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animDismissFirework();
            }
        });
    }

    /**
     * 烟花透明度  渐渐消失
     */
    private void animDismissFirework() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0);
        valueAnimator.addUpdateListener(animation -> {
            isDrawingLauncher = false;
            fireworkAlpha = (float) animation.getAnimatedValue();
            mDrawRunnable.run();
        });
        valueAnimator.setDuration(500);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startLauncherAnim();
            }
        });
    }

    /**
     * 以下三个方法是{@link SurfaceView} 生命周期{@link SurfaceHolder.Callback}的回调
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        if (mDrawRunnable == null) {
            mDrawRunnable = new DrawRunnable(holder, this);
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: 新宽" + width + "新高：" + height);
        this.height = height;
        this.width = width;
        startAnim();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: 销毁");
    }

    /**
     * 异步绘制ui
     */
    private static class DrawRunnable implements Runnable {
        private final SurfaceHolder mSurfaceHolder;
        private final FireworkView mFirework;

        public DrawRunnable(SurfaceHolder surfaceHolder, FireworkView firework) {
            mSurfaceHolder = surfaceHolder;
            mFirework = firework;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            try {
                //获取canvas
                canvas = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    //拦截可能会创建画布失败的情况
                    if (canvas == null) {
                        return;
                    } //做自己的绘制
                    mFirework.doDraw(canvas);
                }
            } catch (Exception e) {
                Log.e("DrawRunnable", "" + e.getMessage());
            } finally {
                if (canvas != null) {
                    //释放canvas 并提交更新到surfaceView上显示
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
