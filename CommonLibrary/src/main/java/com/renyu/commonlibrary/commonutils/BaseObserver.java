package com.renyu.commonlibrary.commonutils;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.renyu.commonlibrary.network.other.NetworkException;
import com.renyu.commonlibrary.views.dialog.NetworkLoadingDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private AppCompatActivity activity;
    private String loadingText;
    private boolean needToast = true;

    protected NetworkLoadingDialog networkLoadingDialog;

    private Disposable d;

    public BaseObserver(boolean needToast) {
        this.needToast = needToast;
    }

    public BaseObserver(AppCompatActivity activity) {
        this.activity = activity;
    }

    public BaseObserver(AppCompatActivity activity, String loadingText) {
        this.activity = activity;
        this.loadingText = loadingText;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (activity!=null) {
            networkLoadingDialog = TextUtils.isEmpty(loadingText)?NetworkLoadingDialog.getInstance():NetworkLoadingDialog.getInstance(loadingText);
            networkLoadingDialog.setDialogDismissListener(() -> networkLoadingDialog = null);
            try {
                networkLoadingDialog.show(activity);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (needToast) {
            if (networkLoadingDialog != null) {
                if (e instanceof NetworkException) {
                    networkLoadingDialog.closeWithText(e.getMessage());
                } else {
                    networkLoadingDialog.closeWithText("网络异常，请稍后再试");
                }
            }
            else {
                if (e instanceof NetworkException) {
                    Toast.makeText(Utils.getApp(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Utils.getApp(), "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onComplete() {

    }

    public void cancelRequest() {
        if (d!=null && !d.isDisposed()) {
            d.dispose();
        }
    }
}
