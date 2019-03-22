package com.renyu.androidcommonlibrary.utils;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.renyu.commonlibrary.dialog.NetworkLoadingDialog;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    private AppCompatActivity activity;
    private String loadingText;

    private NetworkLoadingDialog networkLoadingDialog;

    private Disposable d;

    public BaseObserver() {

    }

    public BaseObserver(AppCompatActivity activity) {
        this.activity = activity;
        this.loadingText = "正在加载中...";
    }

    public BaseObserver(AppCompatActivity activity, String loadingText) {
        this.activity = activity;
        this.loadingText = loadingText;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (activity != null) {
            networkLoadingDialog = TextUtils.isEmpty(loadingText) ? NetworkLoadingDialog.getInstance() : NetworkLoadingDialog.getInstance(loadingText);
            networkLoadingDialog.setDialogDismissListener(() -> {
                networkLoadingDialog = null;
                activity = null;
                cancelRequest();
            });
            try {
                networkLoadingDialog.show(activity);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onError(Throwable e) {
        activity = null;
        ToastUtils.showShort(e.getMessage());
        if (networkLoadingDialog != null) {
            networkLoadingDialog.close();
        }
    }

    @Override
    public void onComplete() {

    }

    public void cancelRequest() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
        d = null;
    }

    public void dismissDialog() {
        if (networkLoadingDialog != null) {
            networkLoadingDialog.close();
        }
    }
}
