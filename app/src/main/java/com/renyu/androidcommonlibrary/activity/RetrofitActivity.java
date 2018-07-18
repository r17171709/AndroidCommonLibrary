package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.widget.Toast;

import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse;
import com.renyu.androidcommonlibrary.api.RetrofitImpl;
import com.renyu.androidcommonlibrary.di.module.ReposModule;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.BaseObserver;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.RetryFunction;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

public class RetrofitActivity extends BaseActivity {
    @Inject
    RetrofitImpl retrofitImpl = null;

    @Override
    public void initParams() {
        ((ExampleApp) (com.blankj.utilcode.util.Utils.getApp())).appComponent.plusAct(new ReposModule()).inject(this);
    }

    @Override
    public int initViews() {
        return 0;
    }

    @Override
    public void loadData() {
        getAccessToken();
    }

    @Override
    public int setStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    private void getAccessToken() {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        String random = "abcdefghijklmn";
        String signature = "app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id=" +
                Utils.getUniquePsuedoID() + "&rand_str=" + random + "&timestamp=" + timestamp;
        retrofitImpl.getAccessToken("nj",
                        timestamp,
                        "46877648",
                        random,
                        Utils.getMD5(signature),
                        Utils.getUniquePsuedoID(),
                        "v1.0",
                        0,
                        1,
                        6000)
                .retryWhen(new RetryFunction(3, 3))
                .compose(Retrofit2Utils.background())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<AccessTokenResponse>(this) {
                    @Override
                    public void onNext(AccessTokenResponse accessTokenResponse) {
                        String access_token = accessTokenResponse.getAccess_token();
                        Toast.makeText(RetrofitActivity.this, access_token, Toast.LENGTH_SHORT).show();

                        networkLoadingDialog.close();
                    }
                });
    }
}
