package com.renyu.commonlibrary.commonutils

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

object LiveDataBus {
    private val eventMap: ConcurrentHashMap<String, StickLiveData<*>> by lazy {
        ConcurrentHashMap<String, StickLiveData<*>>()
    }

    fun <T> with(eventName: String): StickLiveData<T> {
        var liveData = eventMap[eventName]
        return if (liveData != null) {
            liveData as StickLiveData<T>
        } else {
            liveData = StickLiveData<T>(eventName)
            eventMap[eventName] = liveData
            liveData
        }
    }

    class StickLiveData<T>(private val eventName: String) : LiveData<T>() {
        // 事件发送版本号
        var mVersion = 0

        // 粘性数据
        var mStickData: T? = null

        fun setData(data: T) {
            setValue(data)
        }

        fun postData(data: T) {
            postValue(data)
        }

        fun setStickData(data: T) {
            mStickData = data
            setValue(data)
        }

        fun postStickData(data: T) {
            mStickData = data
            postValue(data)
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observeStick(owner, observer, false)
        }

        fun observeStick(owner: LifecycleOwner, observer: Observer<in T>, stick: Boolean = true) {
            super.observe(owner, StickWarpObserver(this@StickLiveData, observer, stick))
            eventMap[eventName] = this
            owner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        // 移除ON_DESTROY下的livedata
                    }
                }
            })
        }
    }

    class StickWarpObserver<T>(
        private val liveData: StickLiveData<T>,
        private val observer: Observer<in T>,
        private val stick: Boolean
    ) : Observer<T> {
        private var mLastVersion = liveData.mVersion
        override fun onChanged(t: T) {
            // 处理回流事件
            if (mLastVersion >= liveData.mVersion) {
                if (stick && liveData.mStickData != null) {
                    observer.onChanged(t)
                }
                return
            }
            observer.onChanged(t)
        }

    }
}