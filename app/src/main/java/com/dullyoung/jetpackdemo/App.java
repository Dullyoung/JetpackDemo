package com.dullyoung.jetpackdemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dullyoung.jetpackdemo.BuildConfig;

/**
 * @Author Dullyoung
 * @Date 2022-06-13
 * @Desp
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }

    }
}
