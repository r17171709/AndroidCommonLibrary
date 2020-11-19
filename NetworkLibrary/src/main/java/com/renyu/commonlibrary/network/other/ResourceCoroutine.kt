package com.renyu.commonlibrary.network.other

/**
 * Created by Administrator on 2018/7/17.
 */
data class ResourceCoroutine<T>(
    val status: Status,
    val data: T?,
    val exception: NetworkException?
) {
    companion object {
        fun <T> sucess(data: T?): ResourceCoroutine<T> {
            return ResourceCoroutine(Status.SUCESS, data, null)
        }

        fun <T> error(exception: NetworkException): ResourceCoroutine<T> {
            return ResourceCoroutine(Status.ERROR, null, exception)
        }

        fun <T> loading(): ResourceCoroutine<T> {
            return ResourceCoroutine(Status.LOADING, null, null)
        }
    }
}