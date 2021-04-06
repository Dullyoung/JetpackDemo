package com.dullyoung.jetpackdemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.dullyoung.jetpackdemo.database.AppDB;
import com.dullyoung.jetpackdemo.database.bean.UserInfo;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "aaaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //room();
        // enqueueWork();
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