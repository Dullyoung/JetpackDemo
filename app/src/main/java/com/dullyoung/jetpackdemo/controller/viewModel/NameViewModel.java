package com.dullyoung.jetpackdemo.controller.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NameViewModel extends ViewModel {
    private MutableLiveData<String> name;

    public MutableLiveData<String> getName() {
        if (name == null) {
            name = new MutableLiveData<String>();
        }
        return name;
    }


    //set & post ,set always run first, post only run last one when have multi post after set
    public void postName() {
        name.postValue("cde");
        name.setValue("abc");
        name.postValue("bcd");
    }
}
