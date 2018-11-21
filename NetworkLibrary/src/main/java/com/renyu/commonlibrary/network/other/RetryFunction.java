package com.renyu.commonlibrary.network.other;

import com.renyu.commonlibrary.network.impl.IRetryCondition;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import java.util.concurrent.TimeUnit;

public class RetryFunction implements Function<Observable<Throwable>, ObservableSource<?>> {

    private int retryDelaySeconds;
    private int retryCountMax;
    private IRetryCondition iRetryCondition;

    private int retryCount;

    private RetryFunction() {
    }

    public RetryFunction(int retryDelaySeconds, int retryCountMax, IRetryCondition iRetryCondition) {
        this.retryDelaySeconds = retryDelaySeconds;
        this.retryCountMax = retryCountMax;
        this.iRetryCondition = iRetryCondition;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (iRetryCondition.canRetry(throwable)) {
                if (++retryCount < retryCountMax) {
                    iRetryCondition.doBeforeRetry();
                    return Observable.timer(retryDelaySeconds, TimeUnit.SECONDS);
                }
            }
            return Observable.error(throwable);
        });
    }
}
