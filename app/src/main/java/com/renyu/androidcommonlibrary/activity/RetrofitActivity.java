package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.api.RetrofitImpl;
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse;
import com.renyu.androidcommonlibrary.di.module.ReposModule;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.BaseObserver;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.RetryFunction;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

public class RetrofitActivity extends RxAppCompatActivity {
    @Inject
    RetrofitImpl retrofitImpl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ExampleApp) (com.blankj.utilcode.util.Utils.getApp())).appComponent.plusAct(new ReposModule()).inject(this);

        getAccessToken();
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
