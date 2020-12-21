package com.renyu.commonlibrary.commonutils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * Created by Administrator on 2018/7/24.
 */
public class RxBus {
    private final Subject<Object> mBus;

    private final Map<Class<?>, Object> mStickyEventMap;

    private RxBus() {
        mBus = PublishSubject.create();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder {
        private static final RxBus sInstance = new RxBus();
    }

    /**
     * 发送事件
     *
     * @param o
     */
    public void post(Object o) {
        mBus.onNext(o);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 发送一个新Sticky事件
     *
     * @param o
     */
    public void postSticky(Object o) {
        mStickyEventMap.put(o.getClass(), o);
        post(o);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservableSticky(Class<T> eventType) {
        Observable<T> observable = mBus.ofType(eventType);
        Object event = mStickyEventMap.get(eventType);
        if (event != null) {
            return observable.mergeWith(Observable.create(emitter -> emitter.onNext(eventType.cast(event))));
        }
        return observable;
    }

    /**
     * 移除指定eventType的Sticky事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
