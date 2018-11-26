package com.renyu.androidcommonlibrary.utils;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;
import com.blankj.utilcode.util.Utils;
import com.renyu.commonlibrary.dialog.NetworkLoadingDialog;
import com.renyu.commonlibrary.network.other.Resource;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver2<T> implements Observer<Resource<T>> {

    public abstract void onError(Resource<T> tResource);
    public abstract void onSucess(Resource<T> tResource);

    private AppCompatActivity activity;
    private String loadingText;
    private boolean needToast = true;

    private NetworkLoadingDialog networkLoadingDialog;

    private Disposable d;

    public BaseObserver2(boolean needToast) {
        this.needToast = needToast;
    }

    public BaseObserver2(AppCompatActivity activity) {
        this.activity = activity;
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
                    if (needToast) {
                        Toast.makeText(Utils.getApp(), tResource.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    onError(tResource);
                    break;
                case SUCESS:
                    onSucess(tResource);
                    break;
                case LOADING:
                    this.d = tResource.getDisposable();
                    if (activity != null) {
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

