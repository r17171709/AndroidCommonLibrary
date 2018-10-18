package com.renyu.commonlibrary.update.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2018/7/24.
 */
public class RxBus {
    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create();
    }

    public static RxBus getDefault() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    public void post(Object o) {
        mBus.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
}
