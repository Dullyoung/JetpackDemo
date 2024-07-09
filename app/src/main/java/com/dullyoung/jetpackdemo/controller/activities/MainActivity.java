package com.dullyoung.jetpackdemo.controller.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dullyoung.baselib.base.BaseActivity;
import com.dullyoung.jetpackdemo.MWorkManager;
import com.dullyoung.jetpackdemo.controller.viewModel.NameViewModel;
import com.dullyoung.jetpackdemo.controller.viewModel.UserInfoViewModel;
import com.dullyoung.jetpackdemo.database.AppDB;
import com.dullyoung.jetpackdemo.database.bean.UserInfo;
import com.dullyoung.jetpackdemo.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static final String TAG = "aaaa";


    @Override
    protected void initViews() {
//        ARouter.getInstance().build("/A/main").withString("key", "I am from app main").navigation();
        //nav2FireworkActivity
        //  startActivity(new Intent(this,FireworkActivity.class));
        mBinding.dl.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //计算缩放百分比，主视图只需要轻微缩放 所以抽屉完全展开 仅缩放10就够了 所以 1 - slideOffset/10
                float percent = 1 - slideOffset / 10;
                mBinding.llMain.setScaleX(percent);
                mBinding.llMain.setScaleY(percent);

                int radius = (int) (slideOffset * 20 * getResources().getDisplayMetrics().density + 0.5f);
                mBinding.cvMain.setRadius(radius);
            }
        });

        //room();
        // enqueueWork();
        mBinding.tvText.setTextColor(Color.RED);
        mBinding.tvText.setOnClickListener(v -> {
            onClickOne();
        });
        mBinding.btnLoading.setOnClickListener(v -> {
            mBinding.pmv.loading();
        });
        mBinding.btnFail.setOnClickListener(v -> {
            mBinding.pmv.onFail();
        });
        mBinding.btnSuccess.setOnClickListener(v -> {
            mBinding.pmv.onSuccess();
        });

        //livedata+vm
        NameViewModel nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);
        Observer<String> nameObserver = s -> {
            mBinding.tvName.setText(s);
            Log.i(TAG, "nameObserver: " + s);
        };
        //跟随生命周期变化 仅当ac在前台运行时执行回调 否则视图不会自动变化 持久监听数据用observeForever
        //nameViewModel.getName().observeForever(nameObserver);
        nameViewModel.getName().observe(this, nameObserver);

        Observer<UserInfo> userInfoObserver = userInfo -> {
            Log.i(TAG, "initViews: " + userInfo);
            mBinding.tvUserPid.setText(userInfo.uid + "");
            mBinding.tvFirstName.setText(userInfo.firstName);
            mBinding.tvAddress.setText(userInfo.address);
        };

        UserInfoViewModel userInfoViewModel = UserInfoViewModel.getInstance(this);
        userInfoViewModel.getUserInfoMutableLiveData().observe(this, userInfoObserver);

        mBinding.btnVmPost.setOnClickListener(view -> {
            nameViewModel.postName();
        });

        mBinding.btnBattery.setOnClickListener(v -> {
            startActivity(BatteryActivity.class);
        });
        mBinding.btnBattery.setOnClickListener(v -> {
            startActivity(WebActivity.class);
        });

        mBinding.btnSetName.setOnClickListener(view -> {
            nameViewModel.getName().setValue(mBinding.etName.getText().toString());
        });
        mBinding.btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        rxTest();
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

    private void rxTest() {

    }
}