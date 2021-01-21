package com.renyu.commonlibrary.commonutils.livedatabus

import androidx.lifecycle.BusLiveData
import androidx.lifecycle.Observer

class LiveDataBusAlwaysActiveObserver<T>(
    private val liveData: BusLiveData<T>,
    private val observer: Observer<in T>
) : BaseLiveDataBusObserverWrapper<T>(liveData, observer) {
}