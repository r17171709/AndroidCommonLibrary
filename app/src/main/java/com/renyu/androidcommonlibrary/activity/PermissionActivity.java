package com.renyu.androidcommonlibrary.activity;

import android.Manifest;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.permission.annotation.NeedPermission;
import com.renyu.commonlibrary.permission.annotation.PermissionDenied;

/**
 * Created by Administrator on 2018/5/25.
 */
public class PermissionActivity extends BaseActivity {
    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_permission;
    }

    @Override
    public void loadData() {
        needPermission();
    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @NeedPermission(permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
            deniedDesp = "请授予存储卡读取权限")
    public void needPermission() {
        Log.d("PermissionActivity", "checkPermission");
    }

    @PermissionDenied(permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void permissionDenied() {
        finish();
    }
}
