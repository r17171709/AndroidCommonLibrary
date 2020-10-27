package com.renyu.commonlibrary.permission.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.renyu.commonlibrary.permission.R;
import com.renyu.commonlibrary.permission.impl.IPermissionStatue;
import com.renyu.commonlibrary.permission.utils.PermissionsUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/5/26.
 *
 * https://www.jianshu.com/p/e94cea26e213
 */
public class PermissionActivity extends AppCompatActivity {
    // 权限接口相关
    public static IPermissionStatue impl;
    private List<String> permission;
    private String deniedDesp;

    private boolean isCheckAgain;
    // 是否需要关闭
    private boolean needDismiss;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        this.permission = Arrays.asList(getIntent().getStringArrayExtra("permissions"));
        this.deniedDesp = getIntent().getStringExtra("deniedDesp");
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isCheckAgain) {
            isCheckAgain = false;
            checkPermission();
        }
    }

    /**
     * 跳转权限授权页面
     * todo 接口传递其实是有问题的
     *
     * @param context
     * @param permissions
     * @param deniedDesp
     * @param impl
     */
    public static void gotoActivity(Context context, String[] permissions, String deniedDesp, IPermissionStatue impl) {
        PermissionActivity.impl = impl;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra("permissions", permissions);
        intent.putExtra("deniedDesp", deniedDesp);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    private void checkPermission() {
        if (permission == null || permission.size() == 0) {
            return;
        }
        String[] permissions = new String[permission.size()];
        for (int i = 0; i < permission.size(); i++) {
            permissions[i] = permission.get(i);
        }
        if (impl != null) {
            if (PermissionsUtils.lacksPermissions(this, permissions)) {
                PermissionsUtils.requestPermissions(this, permissions);
            } else {
                impl.grant();
                impl = null;
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant = true;
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                isGrant = false;
                break;
            }
        }
        if (impl != null) {
            if (isGrant) {
                impl.grant();
                impl = null;
                finish();
                overridePendingTransition(0, 0);
            } else {
                openPermissionDialog();
            }
        }
    }

    public void openPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this);
        builder.setTitle("提示")
                .setCancelable(false)
                .setMessage(deniedDesp)
                .setPositiveButton("确定", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);

                    isCheckAgain = true;
                })
                .setNegativeButton("取消", (dialog, which) -> needDismiss = true)
                .setOnDismissListener(dialog -> {
                    if (needDismiss) {
                        impl.denied();
                        impl = null;
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }).create();
        builder.show();
    }
}
