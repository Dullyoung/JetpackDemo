package com.dullyoung.jetpackdemo.controller.listener;



import com.dullyoung.jetpackdemo.utils.LogUtil;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @Author Dullyoung
 * @Date 2022-05-23
 * @Desp rxjava 3 ob
 */
public abstract class Custom3Observer<T> implements Observer<T> {

    public abstract void onResult(T t);

    @Override
    public void onNext(@NonNull T t) {
        try {
            onResult(t);
        } catch (Throwable throwable) {
            this.onError(throwable);
        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtil.e(e.getStackTrace(), e);
    }

    @Override
    public void onComplete() {

    }
}
