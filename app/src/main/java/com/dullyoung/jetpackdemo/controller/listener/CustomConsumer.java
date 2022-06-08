package com.dullyoung.jetpackdemo.controller.listener;

import androidx.annotation.NonNull;


import com.dullyoung.jetpackdemo.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CustomConsumer<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtil.e(e.getStackTrace(),e);
    }

    @Override
    public void onNext(@NonNull T t) {
        try {
            next(t);
        } catch (Throwable throwable) {
            this.onError(throwable);
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void next(T t);
}
