package com.renyu.commonlibrary.baseact;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.blankj.utilcode.util.PermissionUtils;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

/**
 * Created by Administrator on 2018/7/8.
 */
public abstract class BaseDataBindingActivity<T> extends AppCompatActivity {
    public abstract void initParams();

    public abstract int initViews();

    public abstract void loadData();

    public abstract int setStatusBarColor();

    public abstract int setStatusBarTranslucent();

    public T viewDataBinding;

    // 判断是否执行onCreate以下部分
    public boolean isNeedOnCreate = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNeedOnCreate) {
            return;
        }

        if (initViews() != 0) {
            viewDataBinding = (T) (DataBindingUtil.setContentView(this, initViews()));
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

    public void openLog(String path) {
        String[] permissionsSD = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (PermissionUtils.isGranted(permissionsSD)) {
            // 初始化xlog
            Xlog.open(true, Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", path, InitParams.LOG_NAME, "");
            Xlog.setConsoleLogOpen(true);
            Log.setLogImp(new Xlog());
        }
    }

    public <D extends ViewModel> D getActivityScopeViewModel(Class<D> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }

    public <D extends ViewModel> D getActivityScopeViewModel(Class<D> modelClass, ViewModelProvider.Factory factory) {
        return new ViewModelProvider(this, factory).get(modelClass);
    }

    public <D extends ViewModel, R extends Application & ViewModelStoreOwner> D getApplicationScopeViewModel(Class<D> modelClass, R application) {
        Context context = application.getApplicationContext();
        ViewModelProvider.AndroidViewModelFactory androidViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        return new ViewModelProvider((ViewModelStoreOwner) context, androidViewModelFactory).get(modelClass);
    }
}
