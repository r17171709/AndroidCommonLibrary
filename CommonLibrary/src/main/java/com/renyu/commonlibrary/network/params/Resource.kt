package com.renyu.commonlibrary.network.params

/**
 * Created by Administrator on 2018/7/17.
 */
data class Resource<T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> sucess(data: T?) : Resource<T> {
            return Resource(Status.SUCESS, data, null)
        }

        fun <T> error(message: String?) : Resource<T> {
            return Resource(Status.ERROR, null, message)
        }

        fun <T> loading() : Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}