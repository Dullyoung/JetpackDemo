package com.dullyoung.jetpackdemo.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;


import com.dullyoung.jetpackdemo.Config;
import com.dullyoung.jetpackdemo.R;
import com.dullyoung.jetpackdemo.controller.listener.Custom3Observer;
import com.dullyoung.jetpackdemo.utils.LanguageUtils;
import com.jakewharton.rxbinding4.view.RxView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;


/**
 * Created by @author Dullyoung in  2021/4/6
 */
public abstract class BaseActivity<T extends ViewBinding> extends RxAppCompatActivity {
    protected T mBinding;

    public String TAG = "logger" + this.getClass().getSimpleName();

    protected void setSecureMode() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Type superclass = getClass().getGenericSuperclass();
        Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            mBinding = (T) method.invoke(null, getLayoutInflater());
            setContentView(mBinding.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initViews();
        initImmersionBar();
        setListener();
        loadData();
    }



    @Nullable
    protected  View getStatusBarView(){
        return null;
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setStatusFontColor(boolean dark){
//        ImmersionBar immersionBar = ImmersionBar.with(this);
//        View view = getStatusBarView();
//        if (view != null) {
//            view.setMinimumHeight(BarUtils.getStatusBarHeight());
//            immersionBar.statusBarView(view);
//        }
//        immersionBar.statusBarDarkFont(dark)
//                .keyboardEnable(getKeyBoardEnable())
//                .navigationBarColor(R.color.black)
//                .init();
    }

    protected boolean getKeyBoardEnable(){
        return true;
    }

    private void initImmersionBar() {
//        ImmersionBar immersionBar = ImmersionBar.with(this);
//        View view = getStatusBarView();
//        if (view != null) {
//            view.setMinimumHeight(BarUtils.getStatusBarHeight());
//            immersionBar.statusBarView(view);
//        }
//        immersionBar.statusBarDarkFont(getStatusBarDark())
//                .keyboardEnable(getKeyBoardEnable())
//                .navigationBarColor(R.color.black)
//                .init();
    }


    protected abstract void initViews();

    protected Boolean getStatusBarDark() {
        return false;
    }

    public void startActivity(Class<? extends Activity> ac) {
        Intent intent = new Intent(getContext(), ac);
        startActivity(intent);
    }


    public void startActivityForResult(Class<? extends Activity> ac, int requestCode) {
        Intent intent = new Intent(getContext(), ac);
        startActivityForResult(intent, requestCode);
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

    protected void clickTrigger(Runnable runnable, View... view) {
        for (View v : view) {
            clickTrigger(v, runnable);
        }
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

    protected FragmentActivity getContext() {
        return this;
    }

    protected int color(int color) {
        return ContextCompat.getColor(this, color);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        int language = LanguageUtils.getLanguage();
        Context context = LanguageUtils.attachBaseContext(newBase, language);
        Configuration configuration = context.getResources().getConfiguration();
        // 此处的ContextThemeWrapper是androidx.appcompat.view包下的
        // 你也可以使用android.view.ContextThemeWrapper，但是使用该对象最低只兼容到API 17
        // 所以使用 androidx.appcompat.view.ContextThemeWrapper省心
        Context wrappedContext = new ContextThemeWrapper(context, R.style.Theme_AppCompat_Empty) {
            @Override
            public void applyOverrideConfiguration(Configuration overrideConfiguration) {
                overrideConfiguration.setTo(configuration);
                super.applyOverrideConfiguration(overrideConfiguration);
            }
        };
        super.attachBaseContext(wrappedContext);
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }


}
