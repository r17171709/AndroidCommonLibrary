package com.renyu.commonlibrary.views.permission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.PermissionsUtils;
import com.renyu.commonlibrary.impl.OnPermissionCheckedImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/5/26.
 */
public class PermissionActivity extends BaseActivity {

    // 权限接口相关
    public static OnPermissionCheckedImpl impl;
    private List<String> permission;
    String deniedDesp;

    boolean isCheckAgain;
    // 是否需要关闭
    private boolean needDismiss;

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_permission;
    }

    @Override
    public void loadData() {
        this.permission = Arrays.asList(getIntent().getStringArrayExtra("permissions"));
        this.deniedDesp= getIntent().getStringExtra("deniedDesp");
        checkPermission();
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isCheckAgain) {
            isCheckAgain=false;
            checkPermission();
        }
    }

    /**
     * 跳转权限授权页面
     * todo 接口传递其实是有问题的
     * @param context
     * @param permissions
     * @param deniedDesp
     * @param impl
     */
    public static void gotoActivity(Context context, String[] permissions, String deniedDesp, OnPermissionCheckedImpl impl) {
        PermissionActivity.impl = impl;
        Intent intent=new Intent(context, PermissionActivity.class);
        intent.putExtra("permissions", permissions);
        intent.putExtra("deniedDesp", deniedDesp);
        context.startActivity(intent);
    }

    private void checkPermission() {
        if (permission==null || permission.size()==0) {
            return;
        }
        String[] permissions=new String[permission.size()];
        for (int i = 0; i < permission.size(); i++) {
            permissions[i]=permission.get(i);
        }
        if (impl!=null) {
            if (PermissionsUtils.lacksPermissions(this, permissions)) {
                if (PermissionsUtils.hasDelayAllPermissions(this, permissions)) {
                    openPermissionDialog();
                }
                else {
                    PermissionsUtils.requestPermissions(this, permissions);
                }
            }
            else {
                finish();
                impl.grant();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant=true;
        for (int grantResult : grantResults) {
            if (grantResult== PackageManager.PERMISSION_DENIED) {
                isGrant=false;
                break;
            }
        }
        if (impl!=null) {
            if (isGrant) {
                finish();
                impl.grant();
            }
            else {
                openPermissionDialog();
            }
        }
    }

    public void openPermissionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(PermissionActivity.this);
        builder.setTitle("提示")
                .setMessage(deniedDesp)
                .setPositiveButton("确定", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);

                    isCheckAgain=true;
                })
                .setNegativeButton("取消", (dialog, which) -> needDismiss = true)
                .setOnDismissListener(dialog -> {
                    if (needDismiss) {
                        finish();
                        impl.denied();
                    }
                }).show();
    }
}
