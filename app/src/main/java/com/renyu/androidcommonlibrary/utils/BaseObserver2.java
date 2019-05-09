package com.renyu.androidcommonlibrary.utils;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.renyu.commonlibrary.dialog.NetworkLoadingDialog;
import com.renyu.commonlibrary.network.other.Resource;

import io.reactivex.disposables.Disposable;

public abstract class BaseObserver2<T> implements Observer<Resource<T>> {

    public abstract void onError(Resource<T> tResource);

    public abstract void onSucess(Resource<T> tResource);

    private AppCompatActivity activity;
    private String loadingText;

    private NetworkLoadingDialog networkLoadingDialog;

    private Disposable d;

    public BaseObserver2() {

    }

    public BaseObserver2(AppCompatActivity activity) {
        this.activity = activity;
        this.loadingText = "正在加载中...";
    }

    public BaseObserver2(AppCompatActivity activity, String loadingText) {
        this.activity = activity;
        this.loadingText = loadingText;
    }

    @Override
    public void onChanged(@Nullable Resource<T> tResource) {
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
                    this.d = tResource.getDisposable();
                    if (activity != null && networkLoadingDialog == null) {
                        networkLoadingDialog = TextUtils.isEmpty(loadingText) ? NetworkLoadingDialog.getInstance() : NetworkLoadingDialog.getInstance(loadingText);
                        networkLoadingDialog.setDialogDismissListener(() -> networkLoadingDialog = null);
                        try {
                            networkLoadingDialog.show(activity);
                        } catch (Exception e) {

                        }
                    }
                    break;
            }
        }
    }

    public void cancelRequest() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }
}