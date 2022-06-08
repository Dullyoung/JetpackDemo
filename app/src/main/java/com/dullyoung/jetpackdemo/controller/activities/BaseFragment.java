package com.dullyoung.jetpackdemo.controller.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;


import com.dullyoung.jetpackdemo.Config;
import com.dullyoung.jetpackdemo.R;
import com.dullyoung.jetpackdemo.controller.listener.Custom3Observer;
import com.jakewharton.rxbinding4.view.RxView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;

/*
 *  Created by Dullyoung in  2021/4/6
 */
public abstract class BaseFragment<T extends ViewBinding> extends RxFragment {
    protected T mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            mBinding = (T) method.invoke(null, getLayoutInflater(), container, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return mBinding == null ? null : mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        setListener();
        loadData();

    }

    public boolean getStatusBarDark() {
        return true;
    }

    protected void clickLoginTrigger(View view, Runnable runnable) {
        RxView.clicks(view).throttleFirst(Config.CLICK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Custom3Observer<Unit>() {
                    @Override
                    public void onResult(Unit unit) {
                        runnable.run();
                    }
                });
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

    protected int color(int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    /**
     * 绑定监听事件
     */
    protected void setListener() {

    }

    /**
     * 加载数据
     */
    protected void loadData() {

    }

    protected abstract void initViews();

}
