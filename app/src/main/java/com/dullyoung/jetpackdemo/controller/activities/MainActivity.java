package com.dullyoung.jetpackdemo.controller.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.dullyoung.jetpackdemo.MWorkManager;
import com.dullyoung.jetpackdemo.database.AppDB;
import com.dullyoung.jetpackdemo.database.bean.UserInfo;
import com.dullyoung.jetpackdemo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static final String TAG = "aaaa";


    @Override
    protected void initViews() {
        //room();
        // enqueueWork();
        mBinding.tvText.setTextColor(Color.RED);
        mBinding.tvText.setOnClickListener(v -> {
            onClickOne();
        });
    }

    private void restartLauncher() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }

    public void onClickOne() {
        ComponentName name1 = new ComponentName(this, "com.dullyoung.jetpackdemo.name1");
        ComponentName name2 = new ComponentName(this, "com.dullyoung.jetpackdemo.name2");
        PackageManager pm = getPackageManager();
        // 当前是1 就切换到 2  ，当前是2 就切换到1
        if (pm.getComponentEnabledSetting(name1) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            pm.setComponentEnabledSetting(name1,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(name2,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else {
            pm.setComponentEnabledSetting(name2,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(name1,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }

        restartLauncher();
    }

    //room sql
    private void room() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.firstName = "a" + i;
                    userInfo.lastName = "z" + i;
                    userInfo.address = "翻斗花园" + i + "号";
                    userInfo.age = i * 10 + "";
                    userInfo.uid = i;
                    AppDB.getInstance(MainActivity.this).userDao().insertAll(userInfo);
                }
                Log.i(TAG, "room: " + AppDB.getInstance(MainActivity.this).userDao().getAll());

                UserInfo userInfo = AppDB.getInstance(MainActivity.this).userDao().findByName("a10", "z10");
                AppDB.getInstance(MainActivity.this).userDao().delete(userInfo);
            }
        }.start();
    }

    /**
     * workManager
     */
    private void enqueueWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false).build();
//        WorkRequest workRequest = new PeriodicWorkRequest.Builder(MWorkManager.class,
//                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
//                15, TimeUnit.MINUTES)
//                .setConstraints(constraints)
//                .build();
//
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(MWorkManager.class)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }
}