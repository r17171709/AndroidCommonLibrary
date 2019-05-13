package com.renyu.androidcommonlibrary.utils;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.renyu.commonlibrary.dialog.NetworkLoadingDialog;
import com.renyu.commonlibrary.network.other.Resource;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<Resource<T>> {
    public abstract void onError(Resource<T> tResource);

    public abstract void onSucess(Resource<T> tResource);

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
    public void onChanged(@Nullable Resource<T> tResource) {
        if (tResource != null) {
            switch (tResource.getStatus()) {
                case ERROR:
                    onError(tResource);
                    break;
                case SUCESS:
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

    public void dismissDialog() {
        if (networkLoadingDialog != null) {
            networkLoadingDialog.close();
        }
    }
}