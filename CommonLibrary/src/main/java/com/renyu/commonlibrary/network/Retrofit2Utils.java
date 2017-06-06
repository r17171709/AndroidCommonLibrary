package com.renyu.commonlibrary.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.renyu.commonlibrary.network.params.EmptyResponse;
import com.renyu.commonlibrary.network.params.NetworkException;
import com.renyu.commonlibrary.network.params.Response;
import com.renyu.commonlibrary.network.params.ResponseList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by renyu on 2017/2/3.
 */

public class Retrofit2Utils {

    private static volatile Retrofit2Utils retrofit2Utils;

    private static Retrofit retrofit;
    private static Retrofit retrofitUploadImage;

    private Retrofit2Utils(String baseUrl) {
        // 基础请求
        OkHttpClient.Builder baseOkBuilder=new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        //https默认信任全部证书
        HttpsUtils.SSLParams sslParams=HttpsUtils.getSslSocketFactory(null, null, null);
        baseOkBuilder.hostnameVerifier((hostname, session) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(baseOkBuilder.build()).baseUrl(baseUrl).build();

        // 图片上传请求
        OkHttpClient.Builder imageUploadOkBuilder=new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        retrofitUploadImage=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(imageUploadOkBuilder.build()).baseUrl(baseUrl).build();
    }

    public static void getInstance(String baseUrl) {
        if (retrofit2Utils==null) {
            synchronized (Retrofit2Utils.class) {
                if (retrofit2Utils==null) {
                    retrofit2Utils=new Retrofit2Utils(baseUrl);
                }
            }
        }
    }

    public static Retrofit getBaseRetrofit() {
        return retrofit;
    }

    public static Retrofit getImageUploadRetrofit() {
        return retrofitUploadImage;
    }

    /**
     * post方式提交json数据
     * @param json
     * @return
     */
    public static RequestBody postJsonPrepare(String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON, json);
    }

    public static <T> ObservableTransformer background() {
        return upstream -> upstream
                .flatMap(new Function<Response<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Response<T> response) throws Exception {
                        return Observable.create(e -> {
                            if (response.getResult()==1) {
                                if (response.getData() instanceof EmptyResponse) {
                                    ((EmptyResponse) response.getData()).setMessage(response.getMessage());
                                }
                                e.onNext(response.getData());
                                e.onComplete();
                            }
                            else {
                                NetworkException exception=new NetworkException();
                                exception.setMessage(response.getMessage());
                                exception.setResult(response.getResult());
                                e.onError(exception);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static  <T> ObservableTransformer backgroundList() {
        return upstream -> upstream
                .flatMap(new Function<ResponseList<T>, ObservableSource<List<T>>>() {
                    @Override
                    public ObservableSource<List<T>> apply(ResponseList<T> response) throws Exception {
                        return Observable.create(e -> {
                            if (response.getResult()==1) {
                                e.onNext(response.getData());
                                e.onComplete();
                            }
                            else {
                                NetworkException exception=new NetworkException();
                                exception.setMessage(response.getMessage());
                                exception.setResult(response.getResult());
                                e.onError(exception);
                            }

                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer asyncEmptyBackground() {
        return upstream -> upstream
                .flatMap(new Function<ResponseList<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(ResponseList<T> response) throws Exception {
                        return Observable.create(e -> {
                            if (response.getResult()==1) {
                                EmptyResponse response1=new EmptyResponse();
                                response1.setMessage(response.getMessage());
                                e.onNext((T) response1);
                                e.onComplete();
                            }
                            else {
                                NetworkException exception=new NetworkException();
                                exception.setMessage(response.getMessage());
                                exception.setResult(response.getResult());
                                e.onError(exception);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
