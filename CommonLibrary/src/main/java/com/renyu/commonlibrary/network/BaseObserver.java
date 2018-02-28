package com.renyu.commonlibrary.network;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.renyu.commonlibrary.network.params.NetworkException;
import com.renyu.commonlibrary.views.dialog.NetworkLoadingDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    FragmentActivity activity;
    String loadingText;
    boolean needToast = true;

    public NetworkLoadingDialog networkLoadingDialog;

    Disposable d;

    public BaseObserver(boolean needToast) {
        this.needToast = needToast;
    }

    public BaseObserver(FragmentActivity activity) {
        this.activity = activity;
    }

    public BaseObserver(FragmentActivity activity, String loadingText) {
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
