package com.renyu.commonlibrary.commonutils.livedatabus

import androidx.lifecycle.*

class LiveDataBusLifecycleObserver<T>(
    private val lifecycleOwner: LifecycleOwner,
    private val liveData: BusLiveData<T>,
    private val observer: Observer<in T>
) : BaseLiveDataBusObserverWrapper<T>(liveData, observer), LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        liveData.removeObserver(observer)
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}