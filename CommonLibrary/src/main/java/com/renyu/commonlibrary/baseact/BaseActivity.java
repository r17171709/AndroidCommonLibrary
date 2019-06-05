package com.renyu.commonlibrary.baseact;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.PermissionUtils;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

/**
 * Created by renyu on 2016/12/27.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public abstract void initParams();

    public abstract int initViews();

    public abstract void loadData();

    public abstract int setStatusBarColor();

    public abstract int setStatusBarTranslucent();

    public ProgressDialog networkDialg;

    // 判断是否执行onCreate以下部分
    public boolean isNeedOnCreate = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNeedOnCreate) {
            return;
        }

        if (initViews() != 0) {
            setContentView(initViews());
        }

        // 设置沉浸式，二选一
        if (setStatusBarColor() != 0) {
            BarUtils.setColor(this, setStatusBarColor());
            // 此为全屏模式下设置沉浸式颜色
            // 此方法会导致键盘无法将EditText弹起
//                BarUtils.setColorForSwipeBack(this, setStatusBarColor(), 0);
        }
        if (setStatusBarTranslucent() != 0) {
            BarUtils.setTranslucent(this);
        }
        // 底部导航栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            com.blankj.utilcode.util.BarUtils.setNavBarColor(this, Color.BLACK);
        }

        initParams();
        loadData();
    }

    public void openLog(String path) {
        String[] permissionsSD = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (PermissionUtils.isGranted(permissionsSD)) {
            // 初始化xlog
            Xlog.open(true, Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", path, InitParams.LOG_NAME, "");
            Xlog.setConsoleLogOpen(true);
            Log.setLogImp(new Xlog());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        openLog(InitParams.LOG_PATH);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 关闭xlog，生成日志
        Log.appenderClose();
    }

    public void showNetworkDialog(String content) {
        if (networkDialg == null || !networkDialg.isShowing() && !isFinishing())
            networkDialg = ProgressDialog.show(this, "", content);
    }

    public void dismissNetworkDialog() {
        if (networkDialg != null && networkDialg.isShowing()) {
            networkDialg.dismiss();
            networkDialg = null;
        }
    }
}
