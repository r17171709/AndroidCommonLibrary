package com.renyu.commonlibrary.network;

import com.renyu.commonlibrary.network.impl.IResponse;
import com.renyu.commonlibrary.network.impl.IResponseList;
import com.renyu.commonlibrary.network.other.EmptyResponse;
import com.renyu.commonlibrary.network.other.NetworkException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by renyu on 2017/2/3.
 */

public class Retrofit2Utils {

    // 设置成功码
    public static int sucessedCode = 1;

    /**
     * post方式提交json数据
     *
     * @param json
     * @return
     */
    public static RequestBody postJsonPrepare(String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(JSON, json);
    }

    public static <R> ObservableTransformer<R, R> withSchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<IResponse<T>, T> background() {
        return upstream -> upstream
                .flatMap((Function<IResponse<T>, ObservableSource<T>>) response -> Observable.create(e -> {
                    if (response.getResult() == Retrofit2Utils.sucessedCode) {
                        if (response.getData() instanceof EmptyResponse) {
                            ((EmptyResponse) response.getData()).setMessage(response.getMessage());
                        }
                        e.onNext(response.getData());
                        e.onComplete();
                    } else {
                        NetworkException exception = new NetworkException();
                        exception.setMessage(response.getMessage());
                        exception.setResult(response.getResult());
                        e.onError(exception);
                    }
                }))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof NetworkException) {
                        return Observable.error(throwable);
                    }
                    // 未知异常均转换为NetworkException
                    NetworkException exception = new NetworkException();
                    exception.setMessage(throwable.getMessage());
                    exception.setResult(-1);
                    return Observable.error(exception);
                });
    }

    public static <T> ObservableTransformer<IResponseList<T>, List<T>> backgroundList() {
        return upstream -> upstream
                .flatMap((Function<IResponseList<T>, ObservableSource<List<T>>>) response -> Observable.create(e -> {
                    if (response.getResult() == Retrofit2Utils.sucessedCode) {
                        e.onNext(response.getData());
                        e.onComplete();
                    } else {
                        NetworkException exception = new NetworkException();
                        exception.setMessage(response.getMessage());
                        exception.setResult(response.getResult());
                        e.onError(exception);
                    }

                }))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof NetworkException) {
                        return Observable.error(throwable);
                    }
                    // 未知异常均转换为NetworkException
                    NetworkException exception = new NetworkException();
                    exception.setMessage(throwable.getMessage());
                    exception.setResult(-1);
                    return Observable.error(exception);
                });
    }

    public static <T> ObservableTransformer<IResponseList<T>, T> asyncEmptyBackground() {
        return upstream -> upstream
                .flatMap((Function<IResponseList<T>, ObservableSource<T>>) response -> Observable.create(e -> {
                    if (response.getResult() == Retrofit2Utils.sucessedCode) {
                        EmptyResponse response1 = new EmptyResponse();
                        response1.setMessage(response.getMessage());
                        e.onNext((T) response1);
                        e.onComplete();
                    } else {
                        NetworkException exception = new NetworkException();
                        exception.setMessage(response.getMessage());
                        exception.setResult(response.getResult());
                        e.onError(exception);
                    }
                }))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof NetworkException) {
                        return Observable.error(throwable);
                    }
                    // 未知异常均转换为NetworkException
                    NetworkException exception = new NetworkException();
                    exception.setMessage(throwable.getMessage());
                    exception.setResult(-1);
                    return Observable.error(exception);
                });
    }
}
