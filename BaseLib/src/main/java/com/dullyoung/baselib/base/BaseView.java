package com.dullyoung.baselib.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;


import com.dullyoung.baselib.listener.Custom3Observer;
import com.dullyoung.baselib.utils.LogUtil;
import com.jakewharton.rxbinding4.view.RxView;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;

/**
 * Description : base view
 *
 * @author Dullyoung
 * Date : 2021/12/1  16:21
 */
public abstract class BaseView<T extends ViewBinding> extends ConstraintLayout {

    protected String TAG = "Logger-" + this.getClass().getSimpleName();

    protected T mBinding;

    public BaseView(Context context) {
        super(context);
        initBinding(context);
    }

    private void initBinding(Context context) {
        View v = inflate(context, getLayoutId(), this);
        try {
            //获取父类
            Type superclass = getClass().getGenericSuperclass();
            //获取父类的实参
            Class<?> aClass = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
            //因为在view中 就不能用inflate方法 要用bind
            Method method = aClass.getDeclaredMethod("bind", View.class);
            //方法是静态的 就不必传obj,非静态要传bind方法所属的对象
            mBinding = (T) method.invoke(null, v);
            initViews(context);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBinding(context);
    }

    protected void clickTrigger(View view, Runnable runnable) {
        RxView.clicks(view).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Custom3Observer<Unit>() {
                    @Override
                    public void onResult(Unit unit) {
                        runnable.run();
                    }
                });
    }

    protected void initViews(Context context) {

    }

    public abstract int getLayoutId();

    public String getString(int resId) {
        return getContext().getString(resId);
    }

    protected int getColor(int color) {
        return ContextCompat.getColor(getContext(), color);
    }
}
