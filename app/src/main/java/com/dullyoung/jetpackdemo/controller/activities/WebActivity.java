package com.dullyoung.jetpackdemo.controller.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import com.dullyoung.baselib.base.BaseActivity;
import com.dullyoung.jetpackdemo.IWebProcessInterface;
import com.dullyoung.jetpackdemo.R;
import com.dullyoung.jetpackdemo.databinding.ActivityWebBinding;
import com.dullyoung.jetpackdemo.services.WebProcessService;
import com.dullyoung.jetpackdemo.utils.LogUtil;

public class WebActivity extends BaseActivity<ActivityWebBinding> {

    public static void start(Context context){
        Intent intent =new Intent(context,WebActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTaskDescription(new ActivityManager.TaskDescription("Webview", BitmapFactory.decodeResource(getResources(),
                R.drawable.brvah_sample_footer_loading)));
        supportInvalidateOptionsMenu();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {

        initService();
        clickTrigger(mBinding.btnShow, () -> {
            try {
                LogUtil.i("----------" + Process.myPid());
                mProcessInterface.showToast("哈哈哈哈哈");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    IWebProcessInterface mProcessInterface;
    /**
     * 绑定远程服务RemoteService
     */
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mProcessInterface = IWebProcessInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mProcessInterface = null;
        }
    };

    private void initService() {
        Intent intent = new Intent();
        intent.setClass(getContext(), WebProcessService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }
}