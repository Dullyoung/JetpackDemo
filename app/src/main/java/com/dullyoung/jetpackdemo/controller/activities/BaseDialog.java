package com.dullyoung.jetpackdemo.controller.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ScreenUtils;

import com.dullyoung.jetpackdemo.Config;
import com.dullyoung.jetpackdemo.R;
import com.dullyoung.jetpackdemo.controller.listener.Custom3Observer;
import com.jakewharton.rxbinding4.view.RxView;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;

/**
 * Description :
 *
 * @author Dullyoung
 * Date : 2021/12/6  17:25
 */
public abstract class BaseDialog<T extends ViewBinding> extends Dialog {
    protected T mBinding;

    protected void setSecureMode() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        try {
            View rooView;
            Type superclass = getClass().getGenericSuperclass();
            Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            mBinding = (T) method.invoke(null, getLayoutInflater());
            rooView = mBinding.getRoot();
            setContentView(rooView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rooView.getLayoutParams();
            Point point = new Point();
            getWindow().getWindowManager().getDefaultDisplay().getSize(point);
            int statusBarHeight1 = 0;
            //获取status_bar_height资源的ID
            int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight1 = getContext().getResources().getDimensionPixelSize(resourceId);
            }
            layoutParams.height = ScreenUtils.getScreenHeight() - statusBarHeight1 - getNavigationBarHeight();
            layoutParams.width = point.x;
            rooView.setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCancelable(true);
        initView();

    }

    private int getNavigationBarHeight() {
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    protected void initView() {
    }

    @Override
    public void show() {
        if (ActivityUtils.isActivityAlive(getContext()) && !isShowing()) {
            super.show();
        }
    }


    @Override
    public void dismiss() {
        if (this.isShowing()) {
            super.dismiss();
        }
    }

    protected void clickTrigger(View view, Runnable runnable) {
        RxView.clicks(view).throttleFirst(Config.CLICK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Custom3Observer<Unit>() {
                    @Override
                    public void onResult(Unit unit) {
                        runnable.run();
                    }
                });
    }

    protected String getString(int resId) {
        return getContext().getString(resId);
    }

}
