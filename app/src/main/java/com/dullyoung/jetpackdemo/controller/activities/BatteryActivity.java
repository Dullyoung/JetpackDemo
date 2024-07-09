package com.dullyoung.jetpackdemo.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.BatteryManager;
import android.os.Bundle;

import com.dullyoung.baselib.base.BaseActivity;
import com.dullyoung.jetpackdemo.R;
import com.dullyoung.jetpackdemo.databinding.ActivityBatteryBinding;

public class BatteryActivity extends BaseActivity<ActivityBatteryBinding> {


    @Override
    protected void initViews() {

        BatteryManager batteryManager = getSystemService(BatteryManager.class);
        StringBuilder builder = new StringBuilder();
        builder.append("循环次数:")
                .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER))
                .append("\n")
                .append("以微安为单位的瞬时电池电流:")
                .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW))
                .append("\n")
                .append("平均电池电流（以微安为单位):")
                .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE))
                .append("\n")
                .append("剩余电池容量占总容量的整数百分比:")
                .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY))
                .append("\n")
                .append("电池剩余能量（以纳瓦时为单位）：")
                .append(batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER))
                .append("\n")
                .append("充电状态")
                .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS));
        mBinding.tvText.setText(builder);
    }
}