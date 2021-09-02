package com.dullyoung.jetpackdemo.controller.activities;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dullyoung.jetpackdemo.controller.viewModel.UserInfoViewModel;
import com.dullyoung.jetpackdemo.database.bean.UserInfo;
import com.dullyoung.jetpackdemo.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    @Override
    protected void initViews() {
        UserInfoViewModel userInfoViewModel = UserInfoViewModel.getInstance(this);
        mBinding.btnLogin.setOnClickListener(view -> {
            UserInfo userInfo = new UserInfo();
            userInfo.uid = 1;
            if (!TextUtils.isEmpty(mBinding.etUid.getText().toString())) {
                userInfo.uid = Integer.parseInt(mBinding.etUid.getText().toString());
            }
            userInfo.address = mBinding.etAddress.getText().toString();
            userInfo.firstName = mBinding.etName.getText().toString();
            Log.i("aaaa", "mock login: " + userInfo.toString());
            userInfoViewModel.getUserInfoMutableLiveData().setValue(userInfo);
            Toast.makeText(this, "mock login success", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}