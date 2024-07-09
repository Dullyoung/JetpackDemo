package com.dullyoung.jetpackdemo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;

import androidx.work.multiprocess.IWorkManagerImpl;

import com.dullyoung.jetpackdemo.IWebProcessInterface;
import com.dullyoung.jetpackdemo.utils.LogUtil;

public class WebProcessService extends Service {
    public WebProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new IWebProcessInterface.Stub() {
        @Override
        public void showToast(String str) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(() -> {
                LogUtil.i("----------" + Process.myPid());
            });
        }
    };


}