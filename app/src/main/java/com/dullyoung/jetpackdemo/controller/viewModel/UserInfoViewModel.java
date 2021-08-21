package com.dullyoung.jetpackdemo.controller.viewModel;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dullyoung.jetpackdemo.database.bean.UserInfo;

public class UserInfoViewModel extends ViewModel {
    private static UserInfoViewModel userInfoViewModel;

    //需要是同一个对象才能跨activity通信 暂时用单例做 后面再研究其他api
    public static UserInfoViewModel getInstance(AppCompatActivity activity) {
        if (userInfoViewModel == null) {
            userInfoViewModel = new ViewModelProvider(activity).get(UserInfoViewModel.class);
        }
        return userInfoViewModel;
    }

    private MutableLiveData<UserInfo> userInfoMutableLiveData;

    public MutableLiveData<UserInfo> getUserInfoMutableLiveData() {
        if (userInfoMutableLiveData == null) {
            userInfoMutableLiveData = new MutableLiveData<>();
            UserInfo userInfo = new UserInfo();
            userInfo.uid = 1;
            userInfo.firstName = "default name";
            userInfo.address = "default address";
            userInfoMutableLiveData.setValue(userInfo);
        }
        return userInfoMutableLiveData;
    }

}
