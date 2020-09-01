package com.renyu.commonlibrary.commonutils

import androidx.lifecycle.LiveData
import java.util.*

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBus<T> : LiveData<T>() {
    private var mTask: TimerTask? = null
    private val mTimer by lazy {
        Timer()
    }

    public override fun setValue(value: T) {
        super.setValue(value)
        // 移除重复
        mTask?.cancel()
        mTimer.purge()

        if (value != null) {
            mTask = object : TimerTask() {
                override fun run() {
                    clear()
                }
            }
            mTimer.schedule(mTask, 1000)
        }
    }

    private fun clear() {
        super.postValue(null)
    }
}