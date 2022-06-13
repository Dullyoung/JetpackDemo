package com.dullyoung.modulea;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.dullyoung.baselib.base.BaseActivity;
import com.dullyoung.modulea.databinding.AActivityMainBinding;

@Route(path = "/A/main")
public class MainActivity extends BaseActivity<AActivityMainBinding> {


    @Override
    protected void initViews() {
        ToastUtils.showLong("this is module A");
        mBinding.tvText.setText(getIntent().getStringExtra("key"));
    }
}