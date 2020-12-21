package com.renyu.commonlibrary.network;

import com.renyu.commonlibrary.network.impl.IResponse;
import com.renyu.commonlibrary.network.impl.IResponseList;
import com.renyu.commonlibrary.network.other.AllInfoListResponse;
import com.renyu.commonlibrary.network.other.AllInfoResponse;
import com.renyu.commonlibrary.network.other.EmptyResponse;
import com.renyu.commonlibrary.network.other.NetworkException;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
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

    public static <T> ObservableTransformer<IResponse<T>, AllInfoResponse<T>> backgroundWithAllInfo() {
        return upstream -> upstream
                .flatMap((Function<IResponse<T>, ObservableSource<AllInfoResponse<T>>>) response -> Observable.create(e -> {
                    if (response.getResult() == Retrofit2Utils.sucessedCode) {
                        AllInfoResponse<T> withMessageResponse = new AllInfoResponse<>();
                        withMessageResponse.setData(response.getData());
                        withMessageResponse.setMessage(response.getMessage());
                        withMessageResponse.setResult(withMessageResponse.getResult());
                        e.onNext(withMessageResponse);
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

    public static <T> ObservableTransformer<IResponseList<T>, AllInfoListResponse<T>> backgroundListWithAllInfo() {
        return upstream -> upstream
                .flatMap((Function<IResponseList<T>, ObservableSource<AllInfoListResponse<T>>>) response -> Observable.create(e -> {
                    if (response.getResult() == Retrofit2Utils.sucessedCode) {
                        AllInfoListResponse<T> withMessageResponse = new AllInfoListResponse<>();
                        withMessageResponse.setData(response.getData());
                        withMessageResponse.setMessage(response.getMessage());
                        withMessageResponse.setResult(withMessageResponse.getResult());
                        e.onNext(withMessageResponse);
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

    public static <T> ObservableTransformer<IResponseList<T>, T> emptyBackgroundList() {
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
