package com.renyu.androidcommonlibrary.impl

/**
 * Created by Administrator on 2018/7/10.
 */
interface DataCallBackImpl<T> {
    fun onLoading()
    fun onNext(response: T)
    fun onError(e: Throwable)
}