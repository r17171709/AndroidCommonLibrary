package com.renyu.commonlibrary.commonutils.livedatabus

import androidx.lifecycle.BusLiveData
import androidx.lifecycle.Observer

open class BaseLiveDataBusObserverWrapper<T>(
    private val liveData: BusLiveData<T>,
    private val observer: Observer<in T>
) : Observer<T> {
    private var mLastVersion = liveData.version

    override fun onChanged(t: T) {
        // 处理回流事件
        if (mLastVersion >= liveData.version) {
            return
        }
        observer.onChanged(t)
    }
}