package com.renyu.commonlibrary.network.other

import io.reactivex.disposables.Disposable

/**
 * Created by Administrator on 2018/7/17.
 */
data class Resource<T>(val status: Status, val data: T?, val exception: NetworkException?, val disposable: Disposable?) {
    companion object {
        fun <T> sucess(data: T?) : Resource<T> {
            return Resource(Status.SUCESS, data, null, null)
        }

        fun <T> error(exception: NetworkException) : Resource<T> {
            return Resource(Status.ERROR, null, exception, null)
        }

        fun <T> loading(disposable: Disposable?) : Resource<T> {
            return Resource(Status.LOADING, null, null, disposable)
        }
    }
}