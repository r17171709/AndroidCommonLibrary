package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.api.RetrofitImpl;
import com.renyu.androidcommonlibrary.bean.DataListResponse;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.network.other.AllInfoListResponse;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RetrofitActivity extends AppCompatActivity {
    @Inject
    RetrofitImpl retrofitImpl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ExampleApp) (com.blankj.utilcode.util.Utils.getApp())).appComponent.plusAct().inject(this);

        getAccessToken();
    }

    private void getAccessToken() {
//        int timestamp = (int) (System.currentTimeMillis() / 1000);
//        String random = "abcdefghijklmn";
//        String signature = "app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id=" +
//                Utils.getUniquePsuedoID() + "&rand_str=" + random + "&timestamp=" + timestamp;
//        retrofitImpl.getAccessToken("nj",
//                timestamp,
//                "46877648",
//                random,
//                Utils.getMD5(signature),
//                Utils.getUniquePsuedoID(),
//                "v1.0",
//                0,
//                1,
//                6000)
//                .compose(Retrofit2Utils.backgroundWithAllInfo())
//                .retryWhen(new RetryFunction(3, 3,
//                        new IRetryCondition() {
//                            @Override
//                            public boolean canRetry(Throwable throwable) {
//                                return throwable instanceof NetworkException && ((NetworkException) throwable).getResult() == 1;
//                            }
//
//                            @Override
//                            public void doBeforeRetry() {
//                                try {
//                                    Thread.sleep(2000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }))
//                .compose(Retrofit2Utils.withSchedulers())
//                .compose(bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new BaseObserver<AllInfoResponse<AccessTokenResponse>>(this) {
//                    @Override
//                    public void onNext(AllInfoResponse<AccessTokenResponse> response) {
//                        networkLoadingDialog.setDialogDismissListener(() -> {
//                            finish();
//                            networkLoadingDialog = null;
//                        });
//                        String access_token = response.getData().getAccess_token();
//                        ToastUtils.showShort(access_token);
//                        networkLoadingDialog.close();
//                    }
//                });

        retrofitImpl.getDataList()
                .compose(Retrofit2Utils.backgroundListWithAllInfo())
                .compose(Retrofit2Utils.withSchedulers())
                .subscribe(new Observer<AllInfoListResponse<DataListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllInfoListResponse<DataListResponse> dataListResponseAllInfoListResponse) {
                        ToastUtils.showShort(dataListResponseAllInfoListResponse.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
