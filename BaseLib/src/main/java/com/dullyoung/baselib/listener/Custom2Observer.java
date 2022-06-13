package com.dullyoung.baselib.listener;

import androidx.annotation.NonNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Author Dullyoung
 * @Date 2022-05-23
 * @Desp custom ob
 */
public abstract class Custom2Observer<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
