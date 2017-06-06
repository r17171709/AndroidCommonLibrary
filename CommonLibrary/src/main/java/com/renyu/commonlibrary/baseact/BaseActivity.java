package com.renyu.commonlibrary.baseact;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.commonutils.PermissionsUtils;
import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Retrofit;

/**
 * Created by renyu on 2016/12/27.
 */

public abstract class BaseActivity extends AppCompatActivity {

    // 权限接口相关
    public OnPermissionCheckedListener listener;
    private List<String> permission;
    String deniedDesp;
    public interface OnPermissionCheckedListener {
        void checked(boolean flag);
        void grant();
        void denied();
    }
    boolean isCheckAgain;

    public abstract void initParams();
    public abstract int initViews();
    public abstract void loadData();
    public abstract int setStatusBarColor();
    public abstract int setStatusBarTranslucent();

    // 网络请求
    public OKHttpHelper httpHelper = null;
    public Retrofit retrofit=null;
    public Retrofit retrofitUploadImage;

    public ProgressDialog networkDialg;

    // 判断是否执行onCreate以下部分
    public boolean isNeedOnCreate=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isNeedOnCreate) {
            return;
        }

        if (initViews()!=0) {
            setContentView(initViews());
            ButterKnife.bind(this);
        }

        // 设置沉浸式，二选一
        if (setStatusBarColor()!=0) {
            BarUtils.setColor(this, setStatusBarColor());
            // 此为全屏模式下设置沉浸式颜色
            // 此方法会导致键盘无法将EditText弹起
//                BarUtils.setColorForSwipeBack(this, setStatusBarColor(), 0);
        }
        if (setStatusBarTranslucent()!=0) {
            BarUtils.setTranslucent(this);
        }

        httpHelper = new OKHttpHelper();
        retrofit = Retrofit2Utils.getBaseRetrofit();
        retrofitUploadImage = Retrofit2Utils.getImageUploadRetrofit();

        initParams();
        loadData();
    }

    public void openLog(String path) {
        String[] permissionsSD={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!PermissionsUtils.lacksPermissions(this, permissionsSD)) {
            // 初始化xlog
            Xlog.open(true, Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", path, InitParams.LOG_NAME);
            Xlog.setConsoleLogOpen(true);
            Log.setLogImp(new Xlog());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        openLog(InitParams.LOG_PATH);

        if (isCheckAgain) {
            isCheckAgain=false;
            checkPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 关闭xlog，生成日志
        Log.appenderClose();
    }

    public void checkPermission(String[] permissions, String deniedDesp, OnPermissionCheckedListener listener) {
        this.permission = Arrays.asList(permissions);
        this.deniedDesp=deniedDesp;
        this.listener=listener;
        checkPermission();
    }

    private void checkPermission() {
        if (permission==null || permission.size()==0) {
            return;
        }
        String[] permissions=new String[permission.size()];
        for (int i = 0; i < permission.size(); i++) {
            permissions[i]=permission.get(i);
        }
        if (PermissionsUtils.lacksPermissions(this, permissions)) {
            if (PermissionsUtils.hasDelayAllPermissions(this, permissions)) {
                if (listener!=null) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(BaseActivity.this);
                    builder.setTitle("提示")
                            .setMessage(deniedDesp)
                            .setPositiveButton("确定", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);

                                isCheckAgain=true;
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                if (listener!=null) {
                                    listener.denied();
                                }
                            }).setCancelable(false).show();
                }
            }
            else {
                PermissionsUtils.requestPermissions(this, permissions);
            }
        }
        else {
            if (listener!=null) {
                listener.grant();
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
        if (listener!=null) {
            listener.checked(isGrant);
            if (isGrant) {
                if (listener!=null) {
                    listener.grant();
                }
            }
            else {
                AlertDialog.Builder builder=new AlertDialog.Builder(BaseActivity.this);
                builder.setTitle("提示")
                        .setMessage(deniedDesp)
                        .setPositiveButton("确定", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);

                            isCheckAgain=true;
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            if (listener!=null) {
                                listener.denied();
                            }
                        }).show();
            }
        }
    }

    public void showNetworkDialog(String content) {
        if (networkDialg==null || (networkDialg!=null && !networkDialg.isShowing()) && !isFinishing())
        networkDialg=ProgressDialog.show(this, "", content);
    }

    public void dismissNetworkDialog() {
        if (networkDialg!=null && networkDialg.isShowing()) {
            networkDialg.dismiss();
            networkDialg=null;
        }
    }
}
