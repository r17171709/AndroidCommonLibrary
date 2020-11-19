package com.renyu.androidcommonlibrary.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.ToastUtils;
import com.renyu.commonlibrary.dialog.NetworkLoadingDialog;
import com.renyu.commonlibrary.network.other.ResourceCoroutine;

public abstract class BaseObserver3<T> implements Observer<ResourceCoroutine<T>> {
    public abstract void onError(ResourceCoroutine<T> tResource);

    public abstract void onSucess(ResourceCoroutine<T> tResource);

    private AppCompatActivity activity;
    private String loadingText;

    private NetworkLoadingDialog networkLoadingDialog;

    public BaseObserver3() {

    }

    public BaseObserver3(AppCompatActivity activity) {
        this.activity = activity;
        this.loadingText = "正在加载中...";
    }

    public BaseObserver3(AppCompatActivity activity, String loadingText) {
        this.activity = activity;
        this.loadingText = loadingText;
    }

    @Override
    public void onChanged(@Nullable ResourceCoroutine<T> tResource) {
        if (tResource != null) {
            switch (tResource.getStatus()) {
                case ERROR:
                    if (networkLoadingDialog != null) {
                        networkLoadingDialog.close();
                    }
                    if (tResource.getException().getResult() == -1) {
                        ToastUtils.showShort("网络数据异常,请稍后再试");
                    } else {
                        ToastUtils.showShort(tResource.getException().getMessage());
                    }
                    onError(tResource);
                    break;
                case SUCESS:
                    if (networkLoadingDialog != null) {
                        networkLoadingDialog.close();
                    }
                    onSucess(tResource);
                    break;
                case LOADING:
                    if (activity != null && networkLoadingDialog == null) {
                        networkLoadingDialog = TextUtils.isEmpty(loadingText) ? NetworkLoadingDialog.getInstance() : NetworkLoadingDialog.getInstance(loadingText);
                        networkLoadingDialog.setDialogDismissListener(() -> networkLoadingDialog = null);
                        try {
                            networkLoadingDialog.show(activity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}