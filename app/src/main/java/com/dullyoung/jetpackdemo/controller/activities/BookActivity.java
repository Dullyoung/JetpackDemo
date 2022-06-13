package com.dullyoung.jetpackdemo.controller.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dullyoung.baselib.base.BaseActivity;
import com.dullyoung.jetpackdemo.databinding.ActivityBookBinding;

public class BookActivity extends BaseActivity<ActivityBookBinding> {

    @Nullable
    @Override
    protected View getStatusBarView() {
        return null;
    }

    @Override
    protected void initViews() {

    }
}