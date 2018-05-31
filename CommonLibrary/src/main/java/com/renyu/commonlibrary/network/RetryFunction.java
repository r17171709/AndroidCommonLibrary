package com.renyu.commonlibrary.network;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RetryFunction implements Function<Observable<Throwable>, ObservableSource<?>> {

    private int retryDelaySeconds;
    private int retryCount;
    private int retryCountMax;

    private RetryFunction() {}

    public RetryFunction(int retryDelaySeconds, int retryCountMax) {
        this.retryDelaySeconds = retryDelaySeconds;
        this.retryCountMax = retryCountMax;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (throwable instanceof UnknownHostException) {
                return Observable.error(throwable);
            }
            if (++retryCount < retryCountMax) {
                return Observable.timer(retryDelaySeconds, TimeUnit.SECONDS);
            }
            return Observable.error(throwable);
        });
    }
}
