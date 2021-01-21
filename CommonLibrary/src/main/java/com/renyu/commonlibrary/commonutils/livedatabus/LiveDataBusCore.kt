package com.renyu.commonlibrary.commonutils.livedatabus

import androidx.lifecycle.BusLiveData

class LiveDataBusCore {
    private val mBusMap: MutableMap<String, BusLiveData<*>> by lazy { mutableMapOf() }

    companion object {
        @JvmStatic
        val instance by lazy { LiveDataBusCore() }
    }

    fun <T> getChannel(key: String): BusLiveData<T> {
        return mBusMap.getOrPut(key, {
            BusLiveData<T>()
        }) as BusLiveData<T>
    }
}