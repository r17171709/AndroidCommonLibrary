package com.renyu.androidcommonlibrary.impl

/**
 * Created by Administrator on 2018/7/10.
 */
interface DataActionImpl<T> {
    fun execute(dataCallBackImpl: DataCallBackImpl<T>)
}